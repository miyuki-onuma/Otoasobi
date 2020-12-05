package com.myk.numa.otoasobi.ui.core

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class AppSharePreference(private val context: Context, private val gson: Gson) {

    private val sharedPreferences: SharedPreferences by lazy {
        context.getSharedPreferences("PREF_COMMYKNUMA_OTOASOBI", Context.MODE_PRIVATE)
    }

    operator fun <T> get(
        key: String,
        anonymousClass: Class<T>,
        valueDefaultBoolean: Boolean = false
    ): T? {
        return when (anonymousClass) {
            String::class.java -> sharedPreferences.getString(key, "") as T
            Boolean::class.java -> sharedPreferences.getBoolean(key, valueDefaultBoolean) as T
            Float::class.java -> sharedPreferences.getFloat(key, -1f) as T
            Int::class.java -> sharedPreferences.getInt(key, -1) as T
            Long::class.java -> sharedPreferences.getLong(key, -1L) as T
            else -> gson.fromJson(sharedPreferences.getString(key, ""), anonymousClass)
        }
    }

    fun <T> put(key: String, data: T) {
        val editor = sharedPreferences.edit()
        when (data) {
            is String -> editor.putString(key, data as String)
            is Boolean -> editor.putBoolean(key, data as Boolean)
            is Float -> editor.putFloat(key, data as Float)
            is Int -> editor.putInt(key, data as Int)
            is Long -> editor.putLong(key, data as Long)
            else -> editor.putString(key, gson.toJson(data))
        }
        editor.apply()
    }

    fun remove(key: String) {
        val editor = sharedPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    fun getStringList(key: String): List<String>? {
        try {
            val json =
                sharedPreferences.getString(key, "")
                    ?: return null
            return gson.fromJson(json, object : TypeToken<ArrayList<String?>?>() {}.type)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun putStringList(key: String, name: String) {
        val list = (getStringList(key) ?: emptyList()).toMutableList()
        list.add(name)
        sharedPreferences.edit().putString(key, gson.toJson(list)).apply()
    }
}