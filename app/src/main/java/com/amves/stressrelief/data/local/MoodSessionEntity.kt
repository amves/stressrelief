package com.amves.stressrelief.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a mood logging session.
 */
@Entity(tableName = "mood_sessions")
data class MoodSessionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val moodLevel: Int, // 1 to 10
    val anxietyLevel: Int, // 1 to 10
    val notes: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)
