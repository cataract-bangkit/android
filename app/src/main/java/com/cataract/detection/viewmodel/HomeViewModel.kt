package com.cataract.detection.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.cataract.detection.connection.model.ArticleModel
import com.cataract.detection.connection.room.ArticleRepository

class HomeViewModel(application: Application) : ViewModel() {
    private val articleRepository : ArticleRepository = ArticleRepository(application)

    fun getPortraitArticles(): LiveData<List<ArticleModel>> =
        articleRepository.getPortraitArticles()
}