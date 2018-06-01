package com.kazimad.reditparcer.tools

import android.content.Context
import com.kazimad.reditparcer.BuildConfig

class Prefs(context: Context) {
    //TODO delete class
    private val mContext: Context = context
    private val PREF_NAME = BuildConfig.APPLICATION_ID + ".prefs"

    fun remove(key: String) {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.remove(key)
        prefsEdit.apply()
    }

    fun save(key: String, value: Double) {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        val doubleValue = java.lang.Double.doubleToRawLongBits(value)
        prefsEdit.putLong(key, doubleValue)
        prefsEdit.apply()
    }

    fun save(key: String, value: String) {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putString(key, value)
        prefsEdit.apply()
    }

    fun save(key: String, value: Int) {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putInt(key, value)
        prefsEdit.apply()
    }

    fun save(key: String, value: Boolean) {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putBoolean(key, value)
        prefsEdit.apply()
    }

    fun save(key: String, value: Long) {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val prefsEdit = prefs.edit()
        prefsEdit.putLong(key, value)
        prefsEdit.apply()
    }

    fun getString(key: String): String? {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, "")
    }

    fun getStringWithNull(key: String): String? {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(key, null)
    }

    fun getBoolean(key: String): Boolean {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, false)
    }

    fun getBoolean(key: String, def: Boolean): Boolean {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getBoolean(key, def)
    }

    fun getInt(key: String): Int {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(key, 0)
    }

    fun getInt(key: String, defaultValue: Int): Int {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getInt(key, defaultValue)
    }

    fun getLong(key: String, defaultValue: Long): Long {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(key, defaultValue)
    }

    fun getDouble(key: String): Double {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return java.lang.Double.longBitsToDouble(prefs.getLong(key, 0))
    }

    fun getLong(key: String): Long {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getLong(key, 0L)
    }

    fun isContains(key: String): Boolean {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.contains(key)
    }

    fun getAll(): Map<String, *> {
        val prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.all
    }
}