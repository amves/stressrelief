package com.amves.stressrelief.data.repository

import kotlinx.coroutines.flow.StateFlow

/**
 * Interface for authentication operations.
 */
interface AuthRepository {
    val authState: StateFlow<AuthState>
    
    suspend fun login(email: String, password: String): Result<Unit>
    suspend fun register(email: String, password: String): Result<Unit>
    suspend fun logout()
    fun checkAuthState()
}

/**
 * Represents the current authentication state.
 */
sealed class AuthState {
    object Loading : AuthState()
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
}
