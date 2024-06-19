package com.cataract.detection.connection.room

import android.app.Application
import androidx.lifecycle.LiveData
import com.cataract.detection.connection.model.ArticleModel

class ArticleRepository(application: Application) {
    private val mArticlesDao: ArticleDao

    init {
        val db = ArticleDatabase.getDatabase(application)
        mArticlesDao = db.articleDao()
    }

    fun getPortraitArticles(): LiveData<List<ArticleModel>> =
        mArticlesDao.getPortraitArticles()

    fun getLandscapeArticles(): LiveData<List<ArticleModel>> =
        mArticlesDao.getLandscapeArticles()
}