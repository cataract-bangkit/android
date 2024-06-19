package com.cataract.detection.connection.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.cataract.detection.connection.model.ArticleModel

@Dao
interface ArticleDao {

    @Query("SELECT * FROM article WHERE section = 'portrait'")
    fun getPortraitArticles(): LiveData<List<ArticleModel>>

    @Query("SELECT * FROM article WHERE section = 'landscape'")
    fun getLandscapeArticles(): LiveData<List<ArticleModel>>
}