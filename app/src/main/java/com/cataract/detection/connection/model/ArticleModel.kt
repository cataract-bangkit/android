package com.cataract.detection.connection.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "article")
data class ArticleModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val photo: String,
    val title: String,
    val description: String,
    val writer: String = "Admin",
    val section: String, // 'portrait' or 'landscape'
)
