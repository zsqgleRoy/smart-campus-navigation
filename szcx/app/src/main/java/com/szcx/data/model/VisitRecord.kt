package com.szcx.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "visit_records")
data class VisitRecord(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val websiteId: String,
    val websiteName: String,
    val websiteUrl: String,
    val visitTime: Long
) : Parcelable






