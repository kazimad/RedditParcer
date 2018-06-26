package com.kazimad.reditparcer.remote


class Repository {
    var api: ApiInterface? = null
    fun getApiInterface(): ApiInterface {
        val apiProvider = ApiProvider()
        if (api == null) {
            api =  apiProvider.create(ApiProvider.baseUrl)
        }
        return api!!
    }
}