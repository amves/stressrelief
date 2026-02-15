package com.amves.stressrelief.biometric.core

import kotlin.math.abs
import kotlin.math.sqrt

/**
 * HRV (Heart Rate Variability) Calculator for analyzing R-R intervals.
 * 
 * Provides methods to calculate:
 * - RMSSD: Root Mean Square of Successive Differences
 * - SDNN: Standard Deviation of NN intervals
 * - PNN50: Percentage of successive RR intervals that differ by more than 50ms
 */
object HRVCalculator {

    /**
     * Calculate RMSSD (Root Mean Square of Successive Differences).
     * 
     * @param rr List of R-R intervals in milliseconds
     * @return RMSSD value, or 0.0 if insufficient data
     */
    fun calculateRMSSD(rr: List<Double>): Double {
        if (rr.size < 2) return 0.0
        val diffs = rr.zipWithNext { a, b -> b - a }
        val squared = diffs.map { it * it }
        return sqrt(squared.average())
    }

    /**
     * Calculate SDNN (Standard Deviation of NN intervals).
     * 
     * @param rr List of R-R intervals in milliseconds
     * @return SDNN value, or 0.0 if no data
     */
    fun calculateSDNN(rr: List<Double>): Double {
        if (rr.isEmpty()) return 0.0
        val mean = rr.average()
        return sqrt(rr.map { (it - mean) * (it - mean) }.average())
    }

    /**
     * Calculate PNN50 (Percentage of successive RR intervals differing by more than 50ms).
     * 
     * @param rr List of R-R intervals in milliseconds
     * @return PNN50 percentage (0-100), or 0.0 if insufficient data
     */
    fun calculatePNN50(rr: List<Double>): Double {
        if (rr.size < 2) return 0.0
        val diffs = rr.zipWithNext { a, b -> abs(b - a) }
        val count = diffs.count { it > 50 }
        return (count.toDouble() / diffs.size) * 100
    }
}
