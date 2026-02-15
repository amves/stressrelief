package com.amves.stressrelief.biometric.core

import org.junit.Assert.assertEquals
import org.junit.Test
import kotlin.math.sqrt

class HRVCalculatorTest {

    @Test
    fun `calculateRMSSD returns 0 for empty list`() {
        val result = HRVCalculator.calculateRMSSD(emptyList())
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculateRMSSD returns 0 for single element`() {
        val result = HRVCalculator.calculateRMSSD(listOf(800.0))
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculateRMSSD calculates correctly for two elements`() {
        val rr = listOf(800.0, 850.0)
        val diff = 850.0 - 800.0  // 50.0
        val expected = sqrt(diff * diff)  // sqrt(2500) = 50.0
        val result = HRVCalculator.calculateRMSSD(rr)
        assertEquals(expected, result, 0.0001)
    }

    @Test
    fun `calculateRMSSD calculates correctly for multiple elements`() {
        val rr = listOf(800.0, 810.0, 805.0, 815.0)
        // Differences: 10, -5, 10
        // Squared: 100, 25, 100
        // Average: 75
        // RMSSD: sqrt(75) ≈ 8.66
        val expected = sqrt(75.0)
        val result = HRVCalculator.calculateRMSSD(rr)
        assertEquals(expected, result, 0.0001)
    }

    @Test
    fun `calculateSDNN returns 0 for empty list`() {
        val result = HRVCalculator.calculateSDNN(emptyList())
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculateSDNN returns 0 for single element`() {
        val result = HRVCalculator.calculateSDNN(listOf(800.0))
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculateSDNN calculates correctly for identical elements`() {
        val result = HRVCalculator.calculateSDNN(listOf(800.0, 800.0, 800.0))
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculateSDNN calculates correctly for varying elements`() {
        val rr = listOf(800.0, 810.0, 790.0, 820.0)
        val mean = (800.0 + 810.0 + 790.0 + 820.0) / 4.0  // 805.0
        // Variances: (-5)^2, 5^2, (-15)^2, 15^2 = 25, 25, 225, 225
        // Average variance: 125
        // SDNN: sqrt(125) ≈ 11.18
        val expected = sqrt(125.0)
        val result = HRVCalculator.calculateSDNN(rr)
        assertEquals(expected, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 returns 0 for empty list`() {
        val result = HRVCalculator.calculatePNN50(emptyList())
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 returns 0 for single element`() {
        val result = HRVCalculator.calculatePNN50(listOf(800.0))
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 returns 0 when no differences exceed 50ms`() {
        val rr = listOf(800.0, 810.0, 820.0, 830.0)
        // Differences: 10, 10, 10 - all less than 50
        val result = HRVCalculator.calculatePNN50(rr)
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 returns 100 when all differences exceed 50ms`() {
        val rr = listOf(800.0, 900.0, 1000.0, 1100.0)
        // Differences: 100, 100, 100 - all greater than 50
        val result = HRVCalculator.calculatePNN50(rr)
        assertEquals(100.0, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 calculates correct percentage`() {
        val rr = listOf(800.0, 860.0, 870.0, 930.0, 940.0)
        // Differences: 60, 10, 60, 10
        // Count > 50: 2 out of 4 = 50%
        val result = HRVCalculator.calculatePNN50(rr)
        assertEquals(50.0, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 uses absolute value of differences`() {
        val rr = listOf(900.0, 800.0, 750.0, 850.0)
        // Differences: |-100|, |-50|, |100| = 100, 50, 100
        // Count > 50: 2 out of 3 = 66.67%
        val expected = (2.0 / 3.0) * 100.0
        val result = HRVCalculator.calculatePNN50(rr)
        assertEquals(expected, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 boundary case at exactly 50ms`() {
        val rr = listOf(800.0, 850.0, 900.0)
        // Differences: 50, 50 - exactly at threshold, should not count
        val result = HRVCalculator.calculatePNN50(rr)
        assertEquals(0.0, result, 0.0001)
    }

    @Test
    fun `calculatePNN50 boundary case at 51ms`() {
        val rr = listOf(800.0, 851.0, 902.0)
        // Differences: 51, 51 - just above threshold, should count
        val result = HRVCalculator.calculatePNN50(rr)
        assertEquals(100.0, result, 0.0001)
    }
}
