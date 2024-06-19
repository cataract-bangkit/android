package com.cataract.detection.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cataract.detection.connection.model.ArticleModel
import com.cataract.detection.connection.room.ArticleRepository

class ArticleViewModel(application: Application) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val articleRepository : ArticleRepository = ArticleRepository(application)

    fun getPortraitArticles(): LiveData<List<ArticleModel>> {
        _isLoading.postValue(false)
        return articleRepository.getPortraitArticles()
    }
    fun getLandscapeArticles(): LiveData<List<ArticleModel>> {
        _isLoading.postValue(false)
        return articleRepository.getLandscapeArticles()
    }
}