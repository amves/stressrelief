package com.amves.stressrelief.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Room entity representing a stress relief session with before and after metrics.
 */
@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val stressBefore: Int, // Stress level before session (0-10)
    val stressAfter: Int,  // Stress level after session (0-10)
    val duration: Long,    // Session duration in milliseconds
    val sessionType: String = "general" // Type of stress relief activity
)
