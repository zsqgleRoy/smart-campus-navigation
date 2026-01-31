package com.szcx.data.repository

import com.szcx.data.database.SzcxDatabase
import com.szcx.data.model.Category
import kotlinx.coroutines.flow.Flow

class CategoryRepository(private val database: SzcxDatabase) {
    private val categoryDao = database.categoryDao()
    
    fun getAllCategories(): Flow<List<Category>> = categoryDao.getAllCategories()
    
    suspend fun getCategoryById(id: String): Category? = categoryDao.getCategoryById(id)
    
    suspend fun insertCategories(categories: List<Category>) {
        categoryDao.insertCategories(categories)
    }
    
    suspend fun insertCategory(category: Category) {
        categoryDao.insertCategory(category)
    }
    
    suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(category)
    }
    
    suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(category)
    }
}

