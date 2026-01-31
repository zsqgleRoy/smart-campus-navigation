package com.szcx.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "websites")
data class Website(
    @PrimaryKey val id: String,
    val name: String,
    val url: String,
    val iconUrl: String = "",
    val category: String,
    val description: String = "",
    val isHot: Boolean = false,
    val isFavorite: Boolean = false,
    val accessCount: Int = 0,
    val lastAccess: Long = 0
) : Parcelable






