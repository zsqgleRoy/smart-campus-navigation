package com.szcx.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.szcx.util.PreferencesManager
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferencesManager: PreferencesManager
) : ViewModel() {
    
    val theme: StateFlow<String> = preferencesManager.theme
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "light")
    
    fun setTheme(theme: String) {
        viewModelScope.launch {
            preferencesManager.setTheme(theme)
        }
    }
}






