package com.szcx.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "szcx_preferences")

class PreferencesManager(private val context: Context) {
    companion object {
        private val THEME_KEY = stringPreferencesKey("theme")
        private val FAVORITES_KEY = stringPreferencesKey("favorites")
        private val SEARCH_HISTORY_KEY = stringPreferencesKey("search_history")
        private val USE_ACCESS_COUNT_HOT_KEY = stringPreferencesKey("use_access_count_hot")
    }
    
    val theme: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[THEME_KEY] ?: "light"
    }
    
    suspend fun setTheme(theme: String) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme
        }
    }
    
    val favorites: Flow<String> = context.dataStore.data.map { preferences ->
        preferences[FAVORITES_KEY] ?: ""
    }
    
    suspend fun setFavorites(favorites: String) {
        context.dataStore.edit { preferences ->
            preferences[FAVORITES_KEY] = favorites
        }
    }
    
    val searchHistory: Flow<List<String>> = context.dataStore.data.map { preferences ->
        val history = preferences[SEARCH_HISTORY_KEY] ?: ""
        if (history.isEmpty()) {
            emptyList()
        } else {
            history.split(",").filter { it.isNotEmpty() }
        }
    }
    
    suspend fun addSearchHistory(keyword: String) {
        context.dataStore.edit { preferences ->
            val currentHistory = preferences[SEARCH_HISTORY_KEY] ?: ""
            val historyList = if (currentHistory.isEmpty()) {
                mutableListOf()
            } else {
                currentHistory.split(",").filter { it.isNotEmpty() }.toMutableList()
            }
            // 移除重复项
            historyList.remove(keyword)
            // 添加到开头
            historyList.add(0, keyword)
            // 最多保留10条
            val finalList = historyList.take(10)
            preferences[SEARCH_HISTORY_KEY] = finalList.joinToString(",")
        }
    }
    
    suspend fun clearSearchHistory() {
        context.dataStore.edit { preferences ->
            preferences.remove(SEARCH_HISTORY_KEY)
        }
    }
    
    suspend fun removeSearchHistoryItem(keyword: String) {
        context.dataStore.edit { preferences ->
            val currentHistory = preferences[SEARCH_HISTORY_KEY] ?: ""
            val historyList = if (currentHistory.isEmpty()) {
                mutableListOf()
            } else {
                currentHistory.split(",").filter { it.isNotEmpty() }.toMutableList()
            }
            historyList.remove(keyword)
            if (historyList.isEmpty()) {
                preferences.remove(SEARCH_HISTORY_KEY)
            } else {
                preferences[SEARCH_HISTORY_KEY] = historyList.joinToString(",")
            }
        }
    }
    
    val useAccessCountHot: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[USE_ACCESS_COUNT_HOT_KEY] == "true"
    }
    
    suspend fun setUseAccessCountHot(use: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[USE_ACCESS_COUNT_HOT_KEY] = if (use) "true" else "false"
        }
    }
}

