package com.cataract.detection.connection.service

import android.content.Context
import com.cataract.detection.connection.model.LoginModel

internal class PreferencesService(context: Context) {
    private val preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun setUser(data: LoginModel.Data?): Boolean {
        return if (data != null) {
            val editor = preferences.edit()
            editor.putString("user-login-token",  data.token)
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

    companion object {
        private const val PREF_NAME = "preferences-cataract-setting"
        private const val TAG = "PreferencesService"
    }
}