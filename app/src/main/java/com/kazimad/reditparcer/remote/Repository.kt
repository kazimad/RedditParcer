package com.kazimad.reditparcer.remote

import android.content.Context
import com.kazimad.reditparcer.tools.Prefs


class Repository(context: Context) {


    private val prefs = Prefs(context)
//    private var baseUrl = BASE_URL_CONST_HIT_BTC // defaultUrl


    fun getApiProvider(): ApiProvider = ApiProvider()
    fun getPrefs(): Prefs = prefs
}