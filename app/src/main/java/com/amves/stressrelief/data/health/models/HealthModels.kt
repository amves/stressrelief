package com.amves.stressrelief.data.health.models

import java.time.Instant

/**
 * Represents a heart rate measurement
 */
data class HeartRateMeasurement(
    val beatsPerMinute: Long,
    val timestamp: Instant,
    val source: String = "Unknown"
)

/**
 * Represents RR interval data for HRV calculations
 */
data class RRInterval(
    val intervalMillis: Double,
    val timestamp: Instant
)

/**
 * Represents calculated HRV metrics
 */
data class HRVMetrics(
    val rmssd: Double,
    val sdnn: Double,
    val pnn50: Double,
    val timestamp: Instant,
    val rrIntervalCount: Int
) {
    /**
     * Checks if the metrics are valid (based on sufficient data)
     */
    fun isValid(): Boolean = rrIntervalCount >= 2
}

/**
 * Result wrapper for health data operations
 */
sealed class HealthDataResult<out T> {
    data class Success<T>(val data: T) : HealthDataResult<T>()
    data class Error(val message: String, val exception: Throwable? = null) : HealthDataResult<Nothing>()
    object PermissionDenied : HealthDataResult<Nothing>()
    object NotAvailable : HealthDataResult<Nothing>()
}
