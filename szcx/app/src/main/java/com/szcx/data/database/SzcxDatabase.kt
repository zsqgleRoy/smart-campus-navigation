package com.szcx.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.szcx.data.model.Category
import com.szcx.data.model.VisitRecord
import com.szcx.data.model.Website

@Database(
    entities = [Website::class, Category::class, VisitRecord::class],
    version = 1,
    exportSchema = false
)
abstract class SzcxDatabase : RoomDatabase() {
    abstract fun websiteDao(): WebsiteDao
    abstract fun categoryDao(): CategoryDao
    abstract fun visitRecordDao(): VisitRecordDao
    
    companion object {
        @Volatile
        private var INSTANCE: SzcxDatabase? = null
        
        fun getDatabase(context: Context): SzcxDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SzcxDatabase::class.java,
                    "szcx_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}






