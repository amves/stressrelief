package com.amves.stressrelief.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

/**
 * Room database for the Stress Relief application.
 */
@Database(
    entities = [SessionEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
