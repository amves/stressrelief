package com.amves.stressrelief.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object for session operations.
 */
@Dao
interface SessionDao {
    @Insert
    suspend fun insertSession(session: SessionEntity)
    
    @Query("SELECT * FROM sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<SessionEntity>>
    
    @Query("SELECT * FROM sessions WHERE id = :sessionId")
    suspend fun getSessionById(sessionId: Long): SessionEntity?
    
    @Query("SELECT COUNT(*) FROM sessions")
    fun getSessionCount(): Flow<Int>
    
    @Query("SELECT AVG(stressBefore - stressAfter) FROM sessions")
    fun getAverageStressReduction(): Flow<Double?>
}
