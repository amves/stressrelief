package com.amves.stressrelief.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.amves.stressrelief.data.health.HealthConnectRepository
import com.amves.stressrelief.data.health.WearOSDataService
import com.amves.stressrelief.data.health.models.HRVMetrics
import com.amves.stressrelief.data.health.models.HealthDataResult
import com.amves.stressrelief.data.health.models.HeartRateMeasurement
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel for managing health data and HRV calculations
 */
@HiltViewModel
class HealthViewModel @Inject constructor(
    private val healthConnectRepository: HealthConnectRepository,
    private val wearOSDataService: WearOSDataService
) : ViewModel() {
    
    private val _healthConnectAvailable = MutableStateFlow(false)
    val healthConnectAvailable: StateFlow<Boolean> = _healthConnectAvailable.asStateFlow()
    
    private val _hasPermissions = MutableStateFlow(false)
    val hasPermissions: StateFlow<Boolean> = _hasPermissions.asStateFlow()
    
    private val _wearDeviceConnected = MutableStateFlow(false)
    val wearDeviceConnected: StateFlow<Boolean> = _wearDeviceConnected.asStateFlow()
    
    private val _hrvMetrics = MutableStateFlow<HRVMetrics?>(null)
    val hrvMetrics: StateFlow<HRVMetrics?> = _hrvMetrics.asStateFlow()
    
    private val _heartRateData = MutableStateFlow<List<HeartRateMeasurement>>(emptyList())
    val heartRateData: StateFlow<List<HeartRateMeasurement>> = _heartRateData.asStateFlow()
    
    private val _currentHeartRate = MutableStateFlow<Long?>(null)
    val currentHeartRate: StateFlow<Long?> = _currentHeartRate.asStateFlow()
    
    private val _isMonitoring = MutableStateFlow(false)
    val isMonitoring: StateFlow<Boolean> = _isMonitoring.asStateFlow()
    
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()
    
    init {
        checkHealthConnectAvailability()
        checkPermissions()
        checkWearDevice()
    }
    
    /**
     * Check if Health Connect is available
     */
    fun checkHealthConnectAvailability() {
        viewModelScope.launch {
            _healthConnectAvailable.value = healthConnectRepository.isAvailable()
        }
    }
    
    /**
     * Check if permissions are granted
     */
    fun checkPermissions() {
        viewModelScope.launch {
            _hasPermissions.value = healthConnectRepository.hasAllPermissions()
        }
    }
    
    /**
     * Check if Wear OS device is connected
     */
    fun checkWearDevice() {
        viewModelScope.launch {
            _wearDeviceConnected.value = wearOSDataService.isWearDeviceConnected()
        }
    }
    
    /**
     * Calculate HRV metrics from recent data
     */
    fun calculateHRVMetrics(hoursBack: Long = 24) {
        viewModelScope.launch {
            when (val result = healthConnectRepository.getRecentHRVMetrics(hoursBack)) {
                is HealthDataResult.Success -> {
                    _hrvMetrics.value = result.data
                    _errorMessage.value = null
                }
                is HealthDataResult.Error -> {
                    _errorMessage.value = result.message
                }
                is HealthDataResult.PermissionDenied -> {
                    _errorMessage.value = "Permission denied. Please grant Health Connect permissions."
                    _hasPermissions.value = false
                }
                is HealthDataResult.NotAvailable -> {
                    _errorMessage.value = "Health Connect is not available on this device."
                }
            }
        }
    }
    
    /**
     * Load recent heart rate data
     */
    fun loadHeartRateData(hoursBack: Long = 24) {
        viewModelScope.launch {
            val endTime = java.time.Instant.now()
            val startTime = endTime.minus(hoursBack, java.time.temporal.ChronoUnit.HOURS)
            
            when (val result = healthConnectRepository.readHeartRateData(startTime, endTime)) {
                is HealthDataResult.Success -> {
                    _heartRateData.value = result.data
                    _errorMessage.value = null
                    
                    // Update current heart rate with the most recent value
                    result.data.lastOrNull()?.let {
                        _currentHeartRate.value = it.beatsPerMinute
                    }
                }
                is HealthDataResult.Error -> {
                    _errorMessage.value = result.message
                }
                is HealthDataResult.PermissionDenied -> {
                    _errorMessage.value = "Permission denied. Please grant Health Connect permissions."
                    _hasPermissions.value = false
                }
                is HealthDataResult.NotAvailable -> {
                    _errorMessage.value = "Health Connect is not available on this device."
                }
            }
        }
    }
    
    /**
     * Start heart rate monitoring on Wear OS device
     */
    fun startWearMonitoring() {
        viewModelScope.launch {
            val started = wearOSDataService.startHeartRateMonitoring()
            if (started) {
                _isMonitoring.value = true
                _errorMessage.value = null
                
                // Listen for heart rate updates
                wearOSDataService.listenForHeartRateData().collect { result ->
                    when (result) {
                        is HealthDataResult.Success -> {
                            _currentHeartRate.value = result.data.beatsPerMinute
                            _heartRateData.value = _heartRateData.value + result.data
                        }
                        is HealthDataResult.Error -> {
                            _errorMessage.value = result.message
                        }
                        else -> {}
                    }
                }
            } else {
                _errorMessage.value = "Failed to start monitoring. Is your Wear device connected?"
            }
        }
    }
    
    /**
     * Stop heart rate monitoring on Wear OS device
     */
    fun stopWearMonitoring() {
        viewModelScope.launch {
            wearOSDataService.stopHeartRateMonitoring()
            _isMonitoring.value = false
        }
    }
    
    /**
     * Clear error message
     */
    fun clearError() {
        _errorMessage.value = null
    }
    
    /**
     * Refresh all data
     */
    fun refreshData() {
        checkHealthConnectAvailability()
        checkPermissions()
        checkWearDevice()
        loadHeartRateData()
        calculateHRVMetrics()
    }
}
