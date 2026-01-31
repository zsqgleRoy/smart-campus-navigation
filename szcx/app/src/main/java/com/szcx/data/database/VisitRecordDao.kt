package com.szcx.data.database

import androidx.room.*
import com.szcx.data.model.VisitRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface VisitRecordDao {
    @Query("SELECT * FROM visit_records ORDER BY visitTime DESC LIMIT 50")
    fun getVisitHistory(): Flow<List<VisitRecord>>
    
    @Query("SELECT * FROM visit_records WHERE websiteId = :websiteId ORDER BY visitTime DESC LIMIT 1")
    suspend fun getLatestVisit(websiteId: String): VisitRecord?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVisitRecord(record: VisitRecord)
    
    @Query("DELETE FROM visit_records WHERE id = :id")
    suspend fun deleteVisitRecord(id: Long)
    
    @Query("DELETE FROM visit_records")
    suspend fun clearAllHistory()
    
    @Query("SELECT COUNT(DISTINCT websiteId) FROM visit_records")
    suspend fun getDistinctWebsiteCount(): Int
}

