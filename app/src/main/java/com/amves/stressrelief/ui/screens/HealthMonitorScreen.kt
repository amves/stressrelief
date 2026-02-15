package com.amves.stressrelief.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amves.stressrelief.ui.viewmodels.HealthViewModel
import java.time.format.DateTimeFormatter

/**
 * Screen for Health Connect and Wear OS integration
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HealthMonitorScreen(
    viewModel: HealthViewModel = hiltViewModel()
) {
    val healthConnectAvailable by viewModel.healthConnectAvailable.collectAsState()
    val hasPermissions by viewModel.hasPermissions.collectAsState()
    val wearDeviceConnected by viewModel.wearDeviceConnected.collectAsState()
    val hrvMetrics by viewModel.hrvMetrics.collectAsState()
    val heartRateData by viewModel.heartRateData.collectAsState()
    val currentHeartRate by viewModel.currentHeartRate.collectAsState()
    val isMonitoring by viewModel.isMonitoring.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Health Monitor") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Error message
            errorMessage?.let { error ->
                item {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(onClick = { viewModel.clearError() }) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Dismiss",
                                    tint = MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }
                }
            }
            
            // Status Section
            item {
                StatusCard(
                    healthConnectAvailable = healthConnectAvailable,
                    hasPermissions = hasPermissions,
                    wearDeviceConnected = wearDeviceConnected
                )
            }
            
            // Current Heart Rate
            item {
                CurrentHeartRateCard(
                    heartRate = currentHeartRate,
                    isMonitoring = isMonitoring,
                    onStartMonitoring = { viewModel.startWearMonitoring() },
                    onStopMonitoring = { viewModel.stopWearMonitoring() }
                )
            }
            
            // HRV Metrics
            hrvMetrics?.let { metrics ->
                item {
                    HRVMetricsCard(metrics = metrics)
                }
            }
            
            // Actions
            item {
                Card {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Actions",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        
                        Button(
                            onClick = { viewModel.calculateHRVMetrics() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = hasPermissions
                        ) {
                            Icon(Icons.Default.Info, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Calculate HRV (24h)")
                        }
                        
                        Button(
                            onClick = { viewModel.loadHeartRateData() },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = hasPermissions
                        ) {
                            Icon(Icons.Default.Favorite, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Load Heart Rate Data")
                        }
                    }
                }
            }
            
            // Heart Rate History
            if (heartRateData.isNotEmpty()) {
                item {
                    Text(
                        text = "Recent Heart Rate (${heartRateData.size} readings)",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                
                items(heartRateData.take(10)) { measurement ->
                    HeartRateItem(measurement)
                }
            }
        }
    }
}

@Composable
private fun StatusCard(
    healthConnectAvailable: Boolean,
    hasPermissions: Boolean,
    wearDeviceConnected: Boolean
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "System Status",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            StatusItem(
                label = "Health Connect",
                isAvailable = healthConnectAvailable
            )
            StatusItem(
                label = "Permissions Granted",
                isAvailable = hasPermissions
            )
            StatusItem(
                label = "Wear Device",
                isAvailable = wearDeviceConnected
            )
        }
    }
}

@Composable
private fun StatusItem(label: String, isAvailable: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Icon(
            imageVector = if (isAvailable) Icons.Default.CheckCircle else Icons.Default.Warning,
            contentDescription = null,
            tint = if (isAvailable) 
                MaterialTheme.colorScheme.primary 
            else 
                MaterialTheme.colorScheme.error
        )
    }
}

@Composable
private fun CurrentHeartRateCard(
    heartRate: Long?,
    isMonitoring: Boolean,
    onStartMonitoring: () -> Unit,
    onStopMonitoring: () -> Unit
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Current Heart Rate",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Text(
                text = heartRate?.toString() ?: "--",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.primary
            )
            
            Text(
                text = "BPM",
                style = MaterialTheme.typography.bodyMedium
            )
            
            Button(
                onClick = if (isMonitoring) onStopMonitoring else onStartMonitoring,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    if (isMonitoring) Icons.Default.Close else Icons.Default.PlayArrow,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isMonitoring) "Stop Monitoring" else "Start Monitoring")
            }
        }
    }
}

@Composable
private fun HRVMetricsCard(metrics: com.amves.stressrelief.data.health.models.HRVMetrics) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "HRV Metrics",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            HRVMetricRow("RMSSD", "%.2f ms".format(metrics.rmssd))
            HRVMetricRow("SDNN", "%.2f ms".format(metrics.sdnn))
            HRVMetricRow("PNN50", "%.2f%%".format(metrics.pnn50))
            
            Divider()
            
            Text(
                text = "Data points: ${metrics.rrIntervalCount}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (!metrics.isValid()) {
                Text(
                    text = "⚠️ Insufficient data for accurate HRV",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun HRVMetricRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }
}

@Composable
private fun HeartRateItem(measurement: com.amves.stressrelief.data.health.models.HeartRateMeasurement) {
    Card {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "${measurement.beatsPerMinute} BPM",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = measurement.timestamp.format(
                        DateTimeFormatter.ofPattern("MMM dd, HH:mm")
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            
            Icon(
                Icons.Default.Favorite,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

private fun java.time.Instant.format(formatter: DateTimeFormatter): String {
    return formatter.format(this.atZone(java.time.ZoneId.systemDefault()))
}
