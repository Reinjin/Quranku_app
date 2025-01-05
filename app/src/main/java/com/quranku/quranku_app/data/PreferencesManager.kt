package com.quranku.quranku_app.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

class PreferencesManager(
    @ApplicationContext context: Context
) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        sharedPreferences.edit().putString("LOGIN_TOKEN", token).apply()
    }

    fun getToken(): String? {
        return sharedPreferences.getString("LOGIN_TOKEN", null)
    }

    fun clearToken() {
        sharedPreferences.edit().remove("LOGIN_TOKEN").apply()
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }
}