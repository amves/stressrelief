package com.amves.stressrelief.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.amves.stressrelief.data.repository.AuthState
import com.amves.stressrelief.ui.viewmodels.RootViewModel

/**
 * RootScreen handles authentication routing using StateFlow to track auth state.
 */
@Composable
fun RootScreen(
    viewModel: RootViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    
    LaunchedEffect(Unit) {
        viewModel.checkAuthState()
    }
    
    when (authState) {
        is AuthState.Loading -> {
            LoadingScreen()
        }
        is AuthState.Authenticated -> {
            HomeScreen()
        }
        is AuthState.Unauthenticated -> {
            LoginScreen()
        }
    }
}
