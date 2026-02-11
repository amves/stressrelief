package com.amves.stressrelief.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.amves.stressrelief.R
import com.amves.stressrelief.ui.viewmodels.HomeViewModel

/**
 * HomeScreen provides UI navigation between features: Dashboard, AI Prediction, and Subscription.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Dashboard) }
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    TextButton(onClick = { viewModel.logout() }) {
                        Text(stringResource(R.string.logout))
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { },
                    label = { Text(stringResource(R.string.dashboard)) },
                    selected = selectedScreen == Screen.Dashboard,
                    onClick = { selectedScreen = Screen.Dashboard }
                )
                NavigationBarItem(
                    icon = { },
                    label = { Text(stringResource(R.string.ai_prediction)) },
                    selected = selectedScreen == Screen.AiPrediction,
                    onClick = { selectedScreen = Screen.AiPrediction }
                )
                NavigationBarItem(
                    icon = { },
                    label = { Text(stringResource(R.string.subscription)) },
                    selected = selectedScreen == Screen.Subscription,
                    onClick = { selectedScreen = Screen.Subscription }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedScreen) {
                Screen.Dashboard -> DashboardScreen()
                Screen.AiPrediction -> AiDashboardScreen()
                Screen.Subscription -> SubscriptionScreen()
            }
        }
    }
}

sealed class Screen {
    object Dashboard : Screen()
    object AiPrediction : Screen()
    object Subscription : Screen()
}
