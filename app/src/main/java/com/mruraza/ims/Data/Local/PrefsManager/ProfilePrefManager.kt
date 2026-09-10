package com.mruraza.ims.Data.Local.PrefsManager

import android.content.Context
import android.content.SharedPreferences

class ProfilePrefManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun saveData(key: String, value: String) {
        prefs.edit().putString(key, value).apply()
    }

    fun getData(key: String, default: String = ""): String {
        return prefs.getString(key, default) ?: default
    }
}