package com.cataract.detection.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cataract.detection.connection.service.PreferencesService
import kotlinx.coroutines.launch

class SettingViewModel : ViewModel() {
    private val _messageError = MutableLiveData<String>()
    val messageError: LiveData<String> get() = _messageError

    private val _messageSuccess = MutableLiveData<String>()
    val messageSuccess: LiveData<String> get() = _messageSuccess

    fun logout(context: Context){

        val result = PreferencesService(context).logout()

        if (result) {
            _messageSuccess.postValue("Berhasil Logout")
        } else {
            _messageError.postValue("Ada Masalah Pada Saat Ingin Menghapus User Login")
        }

    }

    fun getThemeSettings(context: Context): LiveData<Boolean> {
        return PreferencesService(context).getThemeSetting().asLiveData()
    }

    fun setThemeSetting(context: Context, isDarkMode: Boolean) {
        viewModelScope.launch {
            PreferencesService(context).setThemeSetting(isDarkMode)
        }
    }
}