package com.kazimad.reditparcer.remote

import com.kazimad.reditparcer.App.Companion.mainComponent

class ApiProvider {
    companion object {
        const val baseUrl = "https://www.reddit.com/"
    }


    fun create(url: String): ApiInterface {
        return mainComponent.getApi()
    }
}