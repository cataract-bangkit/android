package com.cataract.detection.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.cataract.detection.connection.service.PreferencesService

class MainViewModel : ViewModel() {

    private val _loggedIn = MutableLiveData<Boolean>()
    val loggedIn: LiveData<Boolean> = _loggedIn

    fun isUserLoggedIn(context: Context){
        val isLogin = PreferencesService(context).getUserToken()
        _loggedIn.postValue(!isLogin.isNullOrEmpty())
    }
}