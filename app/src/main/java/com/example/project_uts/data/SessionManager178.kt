package com.example.project_uts.data

import android.content.Context

/**
 * Menyimpan sesi login di SharedPreferences. user_id_178 dipakai sebagai
 * kunci utama untuk memfilter data milik pengguna yang sedang masuk.
 */
class SessionManager178(context: Context) {

    private val prefs = context.applicationContext
        .getSharedPreferences("session_178", Context.MODE_PRIVATE)

    fun saveSession(userId: Long, username: String) {
        prefs.edit()
            .putLong(KEY_USER_ID, userId)
            .putString(KEY_USERNAME, username)
            .apply()
    }

    fun getUserId(): Long = prefs.getLong(KEY_USER_ID, NO_USER)

    fun getUsername(): String? = prefs.getString(KEY_USERNAME, null)

    fun isLoggedIn(): Boolean = getUserId() > 0L

    fun logout() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val KEY_USER_ID = "user_id_178"
        private const val KEY_USERNAME = "username_178"
        private const val NO_USER = -1L
    }
}
