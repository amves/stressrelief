# Wear OS and Health Connect Integration Guide

## Overview

This integration enables the StressRelief app to:
- Read heart rate data from Health Connect
- Calculate HRV (Heart Rate Variability) metrics
- Sync with Wear OS devices for real-time heart rate monitoring
- Display health metrics in a dedicated UI

## Architecture

### Components

```
┌─────────────────────────────────────────────────┐
│              Health Monitor Screen              │
│        (HealthMonitorScreen.kt)                │
└────────────────┬────────────────────────────────┘
                 │
                 ▼
┌─────────────────────────────────────────────────┐
│           Health ViewModel                      │
│        (HealthViewModel.kt)                    │
└────────┬──────────────────────┬─────────────────┘
         │                      │
         ▼                      ▼
┌──────────────────┐  ┌──────────────────────────┐
│ Health Connect   │  │   Wear OS Data Service   │
│   Repository     │  │  (WearOSDataService.kt)  │
└────────┬─────────┘  └──────────┬───────────────┘
         │                       │
         ▼                       ▼
┌──────────────────┐  ┌────────────────────────┐
│ Health Connect   │  │   Wear OS Devices      │
│      API         │  │   (Heart Rate)         │
└──────────────────┘  └────────────────────────┘
         │
         ▼
┌──────────────────────────────────┐
│      HRV Calculator              │
│   (HRVCalculator.kt)            │
└──────────────────────────────────┘
```

## Files Created

### 1. Data Models (`data/health/models/HealthModels.kt`)
- `HeartRateMeasurement`: Represents a single heart rate reading
- `RRInterval`: RR interval data for HRV
- `HRVMetrics`: Calculated HRV metrics (RMSSD, SDNN, PNN50)
- `HealthDataResult`: Sealed class for operation results

### 2. Health Connect Repository (`data/health/HealthConnectRepository.kt`)
Manages all Health Connect interactions:
- Check Health Connect availability
- Manage permissions
- Read heart rate data
- Read HRV data
- Calculate HRV metrics using HRVCalculator
- Stream real-time heart rate updates

### 3. Wear OS Data Service (`data/health/WearOSDataService.kt`)
Handles Wear OS device communication:
- Detect connected Wear devices
- Start/stop heart rate monitoring
- Receive heart rate data from Wear OS
- Send data to Wear OS for display

### 4. Health ViewModel (`ui/viewmodels/HealthViewModel.kt`)
Manages UI state and coordinates between repositories:
- Health Connect availability status
- Permission status
- Wear OS connection status
- Heart rate data
- HRV metrics
- Error handling

### 5. Health Monitor Screen (`ui/screens/HealthMonitorScreen.kt`)
Complete UI for health monitoring:
- System status display
- Real-time heart rate monitoring
- HRV metrics visualization
- Heart rate history
- Permission management

### 6. Dependency Injection (`di/HealthModule.kt`)
Hilt module providing health-related dependencies

## Configuration

### Build Dependencies

Added to `app/build.gradle.kts`:

```kotlin
// Health Connect
implementation("androidx.health.connect:connect-client:1.1.0-alpha07")

// Wear OS
implementation("com.google.android.gms:play-services-wearable:18.1.0")
implementation("androidx.wear:wear:1.3.0")

// Coroutines
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
```

### Permissions

Added to `AndroidManifest.xml`:

```xml
<!-- Health Connect permissions -->
<uses-permission android:name="android.permission.health.READ_HEART_RATE" />
<uses-permission android:name="android.permission.health.READ_HEART_RATE_VARIABILITY" />
<uses-permission android:name="android.permission.health.WRITE_HEART_RATE" />

<!-- Body sensors permission -->
<uses-permission android:name="android.permission.BODY_SENSORS" />

<!-- Wear OS permissions -->
<uses-permission android:name="com.google.android.permission.PROVIDE_BACKGROUND_DATA" />
<uses-permission android:name="android.permission.WAKE_LOCK" />
```

## Usage

### 1. Basic Setup

```kotlin
@Composable
fun MyApp() {
    val navController = rememberNavController()
    
    NavHost(navController = navController, startDestination = "home") {
        composable("health") {
            HealthMonitorScreen()
        }
    }
}
```

### 2. Using Health Connect Repository

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val healthRepo: HealthConnectRepository
) : ViewModel() {
    
    fun loadHRV() = viewModelScope.launch {
        val result = healthRepo.getRecentHRVMetrics(hoursBack = 24)
        when (result) {
            is HealthDataResult.Success -> {
                val metrics = result.data
                println("RMSSD: ${metrics.rmssd} ms")
                println("SDNN: ${metrics.sdnn} ms")
                println("PNN50: ${metrics.pnn50}%")
            }
            is HealthDataResult.Error -> {
                println("Error: ${result.message}")
            }
            is HealthDataResult.PermissionDenied -> {
                // Request permissions
            }
            is HealthDataResult.NotAvailable -> {
                // Health Connect not available
            }
        }
    }
}
```

### 3. Using Wear OS Service

```kotlin
@HiltViewModel
class MyViewModel @Inject constructor(
    private val wearService: WearOSDataService
) : ViewModel() {
    
    fun startMonitoring() = viewModelScope.launch {
        // Check if device is connected
        if (wearService.isWearDeviceConnected()) {
            // Start monitoring
            wearService.startHeartRateMonitoring()
            
            // Listen for heart rate data
            wearService.listenForHeartRateData().collect { result ->
                when (result) {
                    is HealthDataResult.Success -> {
                        val hr = result.data
                        println("Heart rate: ${hr.beatsPerMinute} BPM")
                    }
                    else -> {}
                }
            }
        }
    }
}
```

## Permission Handling

### Health Connect Permissions

Users need to grant permissions through Health Connect:

1. App requests permissions using Health Connect's permission controller
2. User is redirected to Health Connect app
3. User grants or denies specific permissions
4. App receives callback with permission status

### Runtime Permission Check

```kotlin
val hasPermissions = healthConnectRepository.hasAllPermissions()
if (!hasPermissions) {
    // Show permission request UI
    // Direct user to Health Connect settings
}
```

## Wear OS Setup

### For Developers

1. **Pair Wear OS Device**:
   - Enable Developer mode on Wear OS device
   - Connect via Bluetooth to phone
   - Enable ADB debugging

2. **Configure Companion App**:
   - Create matching Wear OS module (optional)
   - Use Data Layer API for communication
   - Implement heart rate sensor reading on Wear

3. **Testing**:
   - Use Wear OS emulator
   - Test with physical Wear device
   - Verify data synchronization

## HRV Calculation Details

The integration uses the `HRVCalculator` to compute three key metrics:

### 1. RMSSD (Root Mean Square of Successive Differences)
- **What**: Measures short-term HRV
- **Formula**: sqrt(mean(squared differences between successive RR intervals))
- **Significance**: Higher values indicate better cardiovascular health
- **Typical Range**: 20-50 ms (varies by age and fitness)

### 2. SDNN (Standard Deviation of NN Intervals)
- **What**: Overall HRV measure
- **Formula**: Standard deviation of all RR intervals
- **Significance**: Reflects overall autonomic nervous system activity
- **Typical Range**: 30-100 ms

### 3. PNN50 (Percentage of Successive RR Intervals > 50ms)
- **What**: Percentage of interval differences exceeding 50ms
- **Formula**: (count of differences > 50ms / total differences) × 100
- **Significance**: Indicates parasympathetic activity
- **Typical Range**: 5-30%

### Data Requirements

- **Minimum**: 2 RR intervals (for any calculation)
- **Recommended**: 100+ intervals for accurate HRV (typically 2-5 minutes of data)
- **Optimal**: 5 minutes of stable recording

## Features

### ✅ Implemented

1. **Health Connect Integration**
   - Read heart rate data
   - Read HRV data
   - Permission management
   - Availability checking

2. **Wear OS Integration**
   - Device discovery
   - Data synchronization
   - Real-time monitoring
   - Bidirectional communication

3. **HRV Calculations**
   - RMSSD calculation
   - SDNN calculation
   - PNN50 calculation
   - Automatic estimation from heart rate

4. **UI Components**
   - Health monitor screen
   - Real-time heart rate display
   - HRV metrics cards
   - Status indicators
   - Error handling

5. **Architecture**
   - Repository pattern
   - Dependency injection (Hilt)
   - StateFlow for reactive UI
   - Coroutines for async operations

## Data Flow

### Heart Rate → HRV Calculation

```
1. Wear OS Device / Health Connect
         ↓
2. WearOSDataService / HealthConnectRepository
         ↓
3. HealthViewModel
         ↓
4. Convert to RR Intervals (60000ms / BPM)
         ↓
5. HRVCalculator.calculateRMSSD/SDNN/PNN50
         ↓
6. HRVMetrics (displayed in UI)
```

## Testing

### Unit Tests

The HRV calculation logic is thoroughly tested:
- `HRVCalculatorTest.kt`: 16 test cases covering all scenarios

### Integration Testing

To test the health integration:

1. **Health Connect**:
   - Install Health Connect app on device
   - Grant required permissions
   - Add test heart rate data
   - Verify data reading

2. **Wear OS**:
   - Pair Wear OS device
   - Enable heart rate sensor
   - Start monitoring
   - Verify data synchronization

## Known Limitations

1. **RR Interval Estimation**: 
   - Current implementation estimates RR intervals from heart rate (60000/BPM)
   - For medical-grade HRV, direct RR interval measurement is needed
   - Consider using chest strap or ECG for accurate RR intervals

2. **Health Connect Availability**:
   - Requires Android 9+ (API 28+)
   - Health Connect must be installed
   - Not available on all devices

3. **Wear OS**:
   - Requires paired Wear OS device
   - Heart rate sensor must be available
   - May have latency in data sync

## Future Enhancements

- [ ] Direct RR interval reading from compatible sensors
- [ ] Advanced HRV metrics (frequency domain analysis)
- [ ] Stress level prediction from HRV
- [ ] Historical HRV trends and charts
- [ ] Export HRV data to CSV
- [ ] Background monitoring service
- [ ] Push notifications for abnormal readings
- [ ] Integration with AI stress prediction

## Troubleshooting

### Health Connect Not Available
```kotlin
if (!healthConnectRepository.isAvailable()) {
    // Show message: "Please install Health Connect from Play Store"
}
```

### Permission Denied
```kotlin
if (!healthConnectRepository.hasAllPermissions()) {
    // Redirect to Health Connect permissions
    // Show rationale for permissions
}
```

### No Wear Device Connected
```kotlin
if (!wearOSDataService.isWearDeviceConnected()) {
    // Show message: "Please connect your Wear OS device"
    // Provide link to Wear OS app
}
```

### Insufficient Data for HRV
```kotlin
if (hrvMetrics.rrIntervalCount < 100) {
    // Show warning: "Record at least 2-5 minutes for accurate HRV"
}
```

## Security Considerations

1. **Data Privacy**:
   - Health data is sensitive - handle with care
   - Don't log heart rate or HRV data
   - Follow HIPAA guidelines if applicable

2. **Permissions**:
   - Request minimum necessary permissions
   - Explain why each permission is needed
   - Handle permission denial gracefully

3. **Data Storage**:
   - Consider encryption for stored health data
   - Implement secure data transmission
   - Follow Android security best practices

## References

- [Health Connect Documentation](https://developer.android.com/health-and-fitness/guides/health-connect)
- [Wear OS Documentation](https://developer.android.com/training/wearables)
- [HRV Analysis Guidelines](https://www.frontiersin.org/articles/10.3389/fpubh.2017.00258)
- [Android Health Best Practices](https://developer.android.com/health-and-fitness/guides/health-connect/develop/best-practices)
