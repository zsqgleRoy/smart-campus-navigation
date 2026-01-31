package com.szcx.data.repository

import com.szcx.data.database.SzcxDatabase
import com.szcx.data.model.VisitRecord
import kotlinx.coroutines.flow.Flow

class VisitRecordRepository(private val database: SzcxDatabase) {
    private val visitRecordDao = database.visitRecordDao()
    
    fun getVisitHistory(): Flow<List<VisitRecord>> = visitRecordDao.getVisitHistory()
    
    suspend fun insertVisitRecord(record: VisitRecord) {
        visitRecordDao.insertVisitRecord(record)
    }
    
    suspend fun deleteVisitRecord(id: Long) {
        visitRecordDao.deleteVisitRecord(id)
    }
    
    suspend fun clearAllHistory() {
        visitRecordDao.clearAllHistory()
    }
    
    suspend fun getDistinctWebsiteCount(): Int {
        return visitRecordDao.getDistinctWebsiteCount()
    }
}

