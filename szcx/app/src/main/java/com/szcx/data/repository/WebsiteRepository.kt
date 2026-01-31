package com.szcx.data.repository

import com.szcx.data.database.SzcxDatabase
import com.szcx.data.model.Website
import kotlinx.coroutines.flow.Flow

class WebsiteRepository(private val database: SzcxDatabase) {
    private val websiteDao = database.websiteDao()
    
    fun getAllWebsites(): Flow<List<Website>> = websiteDao.getAllWebsites()
    
    fun getWebsitesByCategory(categoryId: String): Flow<List<Website>> {
        return if (categoryId == "all") {
            websiteDao.getAllWebsites()
        } else {
            websiteDao.getWebsitesByCategory(categoryId)
        }
    }
    
    fun getHotWebsites(): Flow<List<Website>> = websiteDao.getHotWebsites()
    
    fun getHotWebsitesByAccessCount(): Flow<List<Website>> = websiteDao.getHotWebsitesByAccessCount()
    
    fun getFavoriteWebsites(favoriteIds: List<String>): Flow<List<Website>> {
        return websiteDao.getWebsitesByIds(favoriteIds)
    }
    
    fun searchWebsites(keyword: String): Flow<List<Website>> {
        return websiteDao.searchWebsites(keyword)
    }
    
    suspend fun getWebsiteById(id: String): Website? = websiteDao.getWebsiteById(id)
    
    suspend fun getWebsiteByUrl(url: String): Website? = websiteDao.getWebsiteByUrl(url)
    
    suspend fun updateFavoriteStatus(id: String, isFavorite: Boolean) {
        websiteDao.updateFavoriteStatus(id, isFavorite)
    }
    
    suspend fun updateAccessInfo(id: String, timestamp: Long) {
        websiteDao.updateAccessInfo(id, timestamp)
    }
    
    suspend fun insertWebsites(websites: List<Website>) {
        websiteDao.insertWebsites(websites)
    }
    
    suspend fun insertWebsite(website: Website) {
        websiteDao.insertWebsite(website)
    }
    
    suspend fun updateWebsite(website: Website) {
        websiteDao.updateWebsite(website)
    }
    
    suspend fun deleteWebsite(website: Website) {
        websiteDao.deleteWebsite(website)
    }
    
    suspend fun getWebsiteCountByNamePrefix(prefix: String): Int {
        return websiteDao.getWebsiteCountByNamePrefix(prefix)
    }
}

