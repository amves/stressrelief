package com.amves.stressrelief.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for mood session operations.
 */
@Dao
interface MoodSessionDao {

    @Insert
    suspend fun insert(session: MoodSessionEntity)

    @Query("SELECT * FROM mood_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<MoodSessionEntity>>
}
