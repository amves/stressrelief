package com.amves.stressrelief.di

import android.content.Context
import androidx.room.Room
import com.amves.stressrelief.data.local.AppDatabase
import com.amves.stressrelief.data.local.MoodSessionDao
import com.amves.stressrelief.data.local.SessionDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Hilt module providing database dependencies.
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "stress_relief_db"
        )
            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }
    
    @Provides
    fun provideSessionDao(database: AppDatabase): SessionDao {
        return database.sessionDao()
    }

    @Provides
    fun provideMoodSessionDao(database: AppDatabase): MoodSessionDao {
        return database.moodSessionDao()
    }
}
