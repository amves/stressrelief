package com.amves.stressrelief.data.health

import android.content.Context
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.DataClient
import com.google.android.gms.wearable.DataEvent
import com.google.android.gms.wearable.DataEventBuffer
import com.google.android.gms.wearable.DataMapItem
import com.google.android.gms.wearable.MessageClient
import com.google.android.gms.wearable.MessageEvent
import com.google.android.gms.wearable.Node
import com.google.android.gms.wearable.PutDataMapRequest
import com.google.android.gms.wearable.Wearable
import com.amves.stressrelief.data.health.models.HealthDataResult
import com.amves.stressrelief.data.health.models.HeartRateMeasurement
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.time.Instant
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Service for synchronizing health data with Wear OS devices
 */
@Singleton
class WearOSDataService @Inject constructor(
    @ApplicationContext private val context: Context
) {
    
    private val dataClient: DataClient by lazy { Wearable.getDataClient(context) }
    private val messageClient: MessageClient by lazy { Wearable.getMessageClient(context) }
    private val capabilityClient: CapabilityClient by lazy { Wearable.getCapabilityClient(context) }
    
    companion object {
        private const val CAPABILITY_HEART_RATE = "heart_rate_monitoring"
        private const val PATH_HEART_RATE = "/heart_rate"
        private const val PATH_START_MONITORING = "/start_monitoring"
        private const val PATH_STOP_MONITORING = "/stop_monitoring"
        private const val KEY_BPM = "bpm"
        private const val KEY_TIMESTAMP = "timestamp"
    }
    
    /**
     * Find connected Wear OS devices with heart rate capability
     */
    suspend fun findWearDevicesWithHeartRate(): List<Node> {
        return try {
            val capabilityInfo = capabilityClient
                .getCapability(CAPABILITY_HEART_RATE, CapabilityClient.FILTER_REACHABLE)
                .await()
            capabilityInfo.nodes.toList()
        } catch (e: Exception) {
            emptyList()
        }
    }
    
    /**
     * Check if any Wear OS device is connected
     */
    suspend fun isWearDeviceConnected(): Boolean {
        return findWearDevicesWithHeartRate().isNotEmpty()
    }
    
    /**
     * Request Wear OS device to start heart rate monitoring
     */
    suspend fun startHeartRateMonitoring(): Boolean {
        return try {
            val nodes = findWearDevicesWithHeartRate()
            if (nodes.isEmpty()) return false
            
            val tasks = nodes.map { node ->
                messageClient.sendMessage(node.id, PATH_START_MONITORING, ByteArray(0))
            }
            
            Tasks.await(Tasks.whenAll(tasks))
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Request Wear OS device to stop heart rate monitoring
     */
    suspend fun stopHeartRateMonitoring(): Boolean {
        return try {
            val nodes = findWearDevicesWithHeartRate()
            if (nodes.isEmpty()) return false
            
            val tasks = nodes.map { node ->
                messageClient.sendMessage(node.id, PATH_STOP_MONITORING, ByteArray(0))
            }
            
            Tasks.await(Tasks.whenAll(tasks))
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Listen for heart rate data from Wear OS devices
     */
    fun listenForHeartRateData(): Flow<HealthDataResult<HeartRateMeasurement>> = callbackFlow {
        val dataListener = DataClient.OnDataChangedListener { dataEvents: DataEventBuffer ->
            for (event in dataEvents) {
                if (event.type == DataEvent.TYPE_CHANGED && 
                    event.dataItem.uri.path == PATH_HEART_RATE) {
                    
                    try {
                        val dataMap = DataMapItem.fromDataItem(event.dataItem).dataMap
                        val bpm = dataMap.getLong(KEY_BPM)
                        val timestamp = dataMap.getLong(KEY_TIMESTAMP)
                        
                        val measurement = HeartRateMeasurement(
                            beatsPerMinute = bpm,
                            timestamp = Instant.ofEpochMilli(timestamp),
                            source = "Wear OS"
                        )
                        
                        trySend(HealthDataResult.Success(measurement))
                    } catch (e: Exception) {
                        trySend(HealthDataResult.Error("Failed to parse heart rate data", e))
                    }
                }
            }
        }
        
        dataClient.addListener(dataListener)
        
        awaitClose {
            dataClient.removeListener(dataListener)
        }
    }
    
    /**
     * Send heart rate data to Wear OS device (for display)
     */
    suspend fun sendHeartRateToWear(heartRate: Long): Boolean {
        return try {
            val putDataReq = PutDataMapRequest.create(PATH_HEART_RATE).apply {
                dataMap.putLong(KEY_BPM, heartRate)
                dataMap.putLong(KEY_TIMESTAMP, System.currentTimeMillis())
            }.asPutDataRequest()
            
            dataClient.putDataItem(putDataReq).await()
            true
        } catch (e: Exception) {
            false
        }
    }
    
    /**
     * Get all connected Wear OS nodes
     */
    suspend fun getConnectedNodes(): List<Node> {
        return try {
            Wearable.getNodeClient(context).connectedNodes.await()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
