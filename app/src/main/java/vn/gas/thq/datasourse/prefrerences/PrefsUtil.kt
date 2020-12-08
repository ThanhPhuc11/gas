package com.marcotejeda.mvp_retrofit_kotlin.data.datasourse.prefrerences

import android.content.Context
import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty

class PrefsUtil {
    private val NAME = "GasTHQPrefs"
    private lateinit var securePreferences: SharedPreferences

    constructor(context: Context) {
        securePreferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    fun getString(key: String): String {
        return securePreferences.getString(key, "") ?: ""
    }

    fun getString(key: String, defaultValue: String): String {
        return securePreferences.getString(key, defaultValue) ?: defaultValue
    }

    fun setString(key: String, value: String) {
        securePreferences.edit().putString(key, value).commit();
    }

    fun remove(key: String) {
        securePreferences.edit().remove(key).commit()
    }

    fun clear() {
        securePreferences.edit().clear().commit()
    }
}