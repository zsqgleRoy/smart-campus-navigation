package com.szcx

import android.app.Application
import com.szcx.data.database.SzcxDatabase
import com.szcx.data.repository.DataInitializer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class SzcxApplication : Application() {
    val database by lazy { SzcxDatabase.getDatabase(this) }
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    
    override fun onCreate() {
        super.onCreate()
        applicationScope.launch {
            DataInitializer.initializeData(this@SzcxApplication)
        }
    }
}

