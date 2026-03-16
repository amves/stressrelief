package com.amves.stressrelief.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

/**
 * Room database for the Stress Relief application.
 */
@Database(
    entities = [SessionEntity::class, MoodSessionEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
    abstract fun moodSessionDao(): MoodSessionDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS mood_sessions (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        moodLevel INTEGER NOT NULL,
                        anxietyLevel INTEGER NOT NULL,
                        notes TEXT,
                        timestamp INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}
