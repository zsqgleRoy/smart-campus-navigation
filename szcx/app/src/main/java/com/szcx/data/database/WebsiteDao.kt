package com.szcx.data.database

import androidx.room.*
import com.szcx.data.model.Website
import kotlinx.coroutines.flow.Flow

@Dao
interface WebsiteDao {
    @Query("SELECT * FROM websites")
    fun getAllWebsites(): Flow<List<Website>>
    
    @Query("SELECT * FROM websites WHERE category = :categoryId")
    fun getWebsitesByCategory(categoryId: String): Flow<List<Website>>
    
    @Query("SELECT * FROM websites WHERE isHot = 1 LIMIT 8")
    fun getHotWebsites(): Flow<List<Website>>
    
    @Query("SELECT * FROM websites ORDER BY accessCount DESC LIMIT 8")
    fun getHotWebsitesByAccessCount(): Flow<List<Website>>
    
    @Query("SELECT * FROM websites WHERE id IN (:ids)")
    fun getWebsitesByIds(ids: List<String>): Flow<List<Website>>
    
    @Query("SELECT * FROM websites WHERE name LIKE '%' || :keyword || '%' OR description LIKE '%' || :keyword || '%'")
    fun searchWebsites(keyword: String): Flow<List<Website>>
    
    @Query("SELECT * FROM websites WHERE id = :id")
    suspend fun getWebsiteById(id: String): Website?
    
    @Query("SELECT * FROM websites WHERE url = :url LIMIT 1")
    suspend fun getWebsiteByUrl(url: String): Website?
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebsite(website: Website)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWebsites(websites: List<Website>)
    
    @Update
    suspend fun updateWebsite(website: Website)
    
    @Query("UPDATE websites SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean)
    
    @Query("UPDATE websites SET accessCount = accessCount + 1, lastAccess = :timestamp WHERE id = :id")
    suspend fun updateAccessInfo(id: String, timestamp: Long)
    
    @Delete
    suspend fun deleteWebsite(website: Website)
    
    @Query("SELECT COUNT(*) FROM websites WHERE name LIKE :prefix || '%'")
    suspend fun getWebsiteCountByNamePrefix(prefix: String): Int
}

