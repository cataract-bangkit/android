package com.cataract.detection.connection.service

import android.content.Context
import com.cataract.detection.connection.model.LoginModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

internal class PreferencesService(context: Context) {
    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setToken(data: LoginModel.Data?): Boolean {
        return if (data != null) {
            val editor = preferences.edit()
            editor.putString("user-login-token",  data.token)
            editor.apply()
            true
        } else {
            false
        }
    }

    fun setName(name: String): Boolean {
        return if (name != null) {
            val editor = preferences.edit()
            editor.putString("user-login-name",  name)
            editor.apply()
            true
        } else {
            false
        }
    }

    fun logout(): Boolean {
        return try {
            val editor = preferences.edit()
            editor.remove("user-login-token")
            editor.apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun getUserToken(): String? {
        return preferences.getString("user-login-token", null)
    }

    fun getName(): String? {
        return preferences.getString("user-login-name", null)
    }

    fun getThemeSetting(): StateFlow<Boolean> {
        return MutableStateFlow(preferences.getBoolean("dark-mode", false))
    }

    fun setThemeSetting(isDarkMode: Boolean) {
        val editor = preferences.edit()
        editor.putBoolean("dark-mode",  isDarkMode)
        editor.apply()
    }

    companion object {
        private const val PREF_NAME = "preferences-cataract-setting"
        private const val TAG = "PreferencesService"
    }
}