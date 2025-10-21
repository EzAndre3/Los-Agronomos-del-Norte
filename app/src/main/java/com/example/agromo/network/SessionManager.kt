package com.example.agromo.network

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_TOKEN = "jwt_token"
    }

    fun saveToken(token: String) {
        prefs.edit().putString(KEY_TOKEN, token).apply()
    }

    fun getToken(): String? {
        return prefs.getString(KEY_TOKEN, null)
    }

    fun saveEmail(email: String) {
        prefs.edit().putString("email", email).apply()
    }

    fun getSavedEmail(): String? = prefs.getString("email", null)

    fun clearEmail() {
        prefs.edit().remove("email").apply()
    }

    fun clearSession() {
        prefs.edit()
            // 🔽 cambió solo esta línea
            .remove(KEY_TOKEN) // antes era "token", que no existe en tus prefs
            .remove("email")
            .remove("username")
            .apply()
    }

    fun saveNombre(email: String, nombre: String) {
        prefs.edit().putString("nombre_$email", nombre).apply()
    }

    fun getNombre(email: String): String? {
        return prefs.getString("nombre_$email", null)
    }

    fun saveUsername(username: String) {
        prefs.edit().putString("username", username).apply()
    }

    fun getUsername(): String? {
        return prefs.getString("username", null)
    }
}

