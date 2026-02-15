package com.amves.stressrelief.data.health

import android.content.Context
import androidx.health.connect.client.HealthConnectClient
import androidx.health.connect.client.permission.HealthPermission
import androidx.health.connect.client.records.HeartRateRecord
import androidx.health.connect.client.records.HeartRateVariabilityRmssdRecord
import androidx.health.connect.client.request.ReadRecordsRequest
import androidx.health.connect.client.time.TimeRangeFilter
import com.amves.stressrelief.biometric.core.HRVCalculator
import com.amves.stressrelief.data.health.models.HRVMetrics
import com.amves.stressrelief.data.health.models.HealthDataResult
import com.amves.stressrelief.data.health.models.HeartRateMeasurement
import com.amves.stressrelief.data.health.models.RRInterval
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.Instant
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for accessing Health Connect data
 */
@Singleton
class HealthConnectRepository @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val healthConnectClient by lazy {
        HealthConnectClient.getOrCreate(context)
    }
    
    companion object {
        val REQUIRED_PERMISSIONS = setOf(
            HealthPermission.getReadPermission(HeartRateRecord::class),
            HealthPermission.getReadPermission(HeartRateVariabilityRmssdRecord::class)
        )
    }
    
    /**
     * Check if Health Connect is available on this device
     */
    suspend fun isAvailable(): Boolean {
        return HealthConnectClient.getSdkStatus(context) == HealthConnectClient.SDK_AVAILABLE
    }
    
    /**
     * Check if all required permissions are granted
     */
    suspend fun hasAllPermissions(): Boolean {
        return try {
            val granted = healthConnectClient.permissionController.getGrantedPermissions()
            REQUIRED_PERMISSIONS.all { it in granted }
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Read heart rate data for the specified time range
     */
    suspend fun readHeartRateData(
        startTime: Instant,
        endTime: Instant = Instant.now()
    ): HealthDataResult<List<HeartRateMeasurement>> {
        return try {
            if (!hasAllPermissions()) {
                return HealthDataResult.PermissionDenied
            }
            
            val request = ReadRecordsRequest(
                recordType = HeartRateRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            
            val response = healthConnectClient.readRecords(request)
            val measurements = response.records.flatMap { record ->
                record.samples.map { sample ->
                    HeartRateMeasurement(
                        beatsPerMinute = sample.beatsPerMinute,
                        timestamp = sample.time,
                        source = record.metadata.dataOrigin.packageName
                    )
                }
            }.sortedBy { it.timestamp }
            
            HealthDataResult.Success(measurements)
        } catch (e: Exception) {
            HealthDataResult.Error("Failed to read heart rate data", e)
        }
    }
    
    /**
     * Read HRV RMSSD data from Health Connect
     */
    suspend fun readHRVData(
        startTime: Instant,
        endTime: Instant = Instant.now()
    ): HealthDataResult<List<Double>> {
        return try {
            if (!hasAllPermissions()) {
                return HealthDataResult.PermissionDenied
            }
            
            val request = ReadRecordsRequest(
                recordType = HeartRateVariabilityRmssdRecord::class,
                timeRangeFilter = TimeRangeFilter.between(startTime, endTime)
            )
            
            val response = healthConnectClient.readRecords(request)
            val rmssdValues = response.records.map { it.heartRateVariabilityMillis }
            
            HealthDataResult.Success(rmssdValues)
        } catch (e: Exception) {
            HealthDataResult.Error("Failed to read HRV data", e)
        }
    }
    
    /**
     * Calculate HRV metrics from heart rate data
     * 
     * Note: This method estimates RR intervals from heart rate data.
     * For more accurate HRV, use direct RR interval data from a chest strap or ECG.
     */
    suspend fun calculateHRVMetrics(
        startTime: Instant,
        endTime: Instant = Instant.now()
    ): HealthDataResult<HRVMetrics> {
        return try {
            // Read heart rate data
            val heartRateResult = readHeartRateData(startTime, endTime)
            
            if (heartRateResult !is HealthDataResult.Success) {
                return when (heartRateResult) {
                    is HealthDataResult.PermissionDenied -> HealthDataResult.PermissionDenied
                    is HealthDataResult.Error -> heartRateResult
                    else -> HealthDataResult.Error("Unknown error")
                }
            }
            
            val heartRates = heartRateResult.data
            if (heartRates.size < 2) {
                return HealthDataResult.Error("Insufficient heart rate data for HRV calculation")
            }
            
            // Estimate RR intervals from heart rate (60000ms / BPM)
            val rrIntervals = heartRates.map { hr ->
                60000.0 / hr.beatsPerMinute.toDouble()
            }
            
            // Calculate HRV metrics using HRVCalculator
            val rmssd = HRVCalculator.calculateRMSSD(rrIntervals)
            val sdnn = HRVCalculator.calculateSDNN(rrIntervals)
            val pnn50 = HRVCalculator.calculatePNN50(rrIntervals)
            
            val metrics = HRVMetrics(
                rmssd = rmssd,
                sdnn = sdnn,
                pnn50 = pnn50,
                timestamp = Instant.now(),
                rrIntervalCount = rrIntervals.size
            )
            
            HealthDataResult.Success(metrics)
        } catch (e: Exception) {
            HealthDataResult.Error("Failed to calculate HRV metrics", e)
        }
    }
    
    /**
     * Get HRV metrics for the last N hours
     */
    suspend fun getRecentHRVMetrics(hoursBack: Long = 24): HealthDataResult<HRVMetrics> {
        val endTime = Instant.now()
        val startTime = endTime.minus(hoursBack, ChronoUnit.HOURS)
        return calculateHRVMetrics(startTime, endTime)
    }
    
    /**
     * Stream heart rate data updates
     */
    fun streamHeartRateData(intervalMinutes: Long = 5): Flow<HealthDataResult<List<HeartRateMeasurement>>> = flow {
        while (true) {
            val endTime = Instant.now()
            val startTime = endTime.minus(intervalMinutes, ChronoUnit.MINUTES)
            val result = readHeartRateData(startTime, endTime)
            emit(result)
            kotlinx.coroutines.delay(intervalMinutes * 60 * 1000)
        }
    }
}
