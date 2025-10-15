package com.example.agromo.network

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("agromo_session", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("token", token).apply()
    }

    fun getToken(): String? = prefs.getString("token", null)

    fun saveEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getSavedEmail(): String? = prefs.getString("email", null)

    fun clearEmail() {
        prefs.edit().remove("email").apply()
    }

    fun clearSession() {
        prefs.edit().clear().apply()
    }
}
