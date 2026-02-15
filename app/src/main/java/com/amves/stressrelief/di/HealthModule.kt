package com.amves.stressrelief.di

import android.content.Context
import com.amves.stressrelief.data.health.HealthConnectRepository
import com.amves.stressrelief.data.health.WearOSDataService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module for providing health-related dependencies
 */
@Module
@InstallIn(SingletonComponent::class)
object HealthModule {
    
    @Provides
    @Singleton
    fun provideHealthConnectRepository(
        @ApplicationContext context: Context
    ): HealthConnectRepository {
        return HealthConnectRepository(context)
    }
    
    @Provides
    @Singleton
    fun provideWearOSDataService(
        @ApplicationContext context: Context
    ): WearOSDataService {
        return WearOSDataService(context)
    }
}
