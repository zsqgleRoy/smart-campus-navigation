package com.szcx.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.szcx.data.database.SzcxDatabase
import com.szcx.data.repository.*
import com.szcx.ui.addwebsite.AddWebsiteViewModel
import com.szcx.ui.category.CategoryManageViewModel
import com.szcx.ui.category.CategoryViewModel
import com.szcx.ui.editwebsite.EditWebsiteViewModel
import com.szcx.ui.favorites.FavoritesViewModel
import com.szcx.ui.home.HomeViewModel
import com.szcx.ui.search.SearchViewModel
import com.szcx.ui.settings.SettingsViewModel
import com.szcx.util.PreferencesManager

class ViewModelFactory(
    private val database: SzcxDatabase,
    private val preferencesManager: PreferencesManager
) : ViewModelProvider.Factory {
    
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(
                    WebsiteRepository(database),
                    CategoryRepository(database),
                    VisitRecordRepository(database),
                    preferencesManager
                ) as T
            }
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> {
                CategoryViewModel(
                    WebsiteRepository(database),
                    CategoryRepository(database),
                    VisitRecordRepository(database),
                    preferencesManager
                ) as T
            }
            modelClass.isAssignableFrom(FavoritesViewModel::class.java) -> {
                FavoritesViewModel(
                    WebsiteRepository(database),
                    VisitRecordRepository(database),
                    preferencesManager
                ) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(
                    WebsiteRepository(database),
                    VisitRecordRepository(database),
                    preferencesManager
                ) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(preferencesManager) as T
            }
            modelClass.isAssignableFrom(AddWebsiteViewModel::class.java) -> {
                AddWebsiteViewModel(
                    WebsiteRepository(database),
                    CategoryRepository(database)
                ) as T
            }
            modelClass.isAssignableFrom(EditWebsiteViewModel::class.java) -> {
                EditWebsiteViewModel(
                    WebsiteRepository(database),
                    CategoryRepository(database)
                ) as T
            }
            modelClass.isAssignableFrom(CategoryManageViewModel::class.java) -> {
                CategoryManageViewModel(
                    CategoryRepository(database)
                ) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}

