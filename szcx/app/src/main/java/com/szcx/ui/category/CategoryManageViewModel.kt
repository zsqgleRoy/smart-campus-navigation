package com.szcx.ui.category

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.data.model.Category
import com.szcx.data.repository.CategoryRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.UUID

class CategoryManageViewModel(
    private val categoryRepository: CategoryRepository
) : ViewModel() {
    
    val categories = categoryRepository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    
    fun addCategory(name: String, iconRes: String, color: String) {
        viewModelScope.launch {
            val category = Category(
                id = UUID.randomUUID().toString(),
                name = name,
                iconRes = iconRes,
                color = color
            )
            categoryRepository.insertCategory(category)
        }
    }
    
    fun updateCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.updateCategory(category)
        }
    }
    
    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            categoryRepository.deleteCategory(category)
        }
    }
}






