package com.kazimad.reditparcer.remote

import com.google.gson.GsonBuilder
import com.kazimad.reditparcer.App.Companion.mainComponent
import com.kazimad.reditparcer.BuildConfig
import com.kazimad.reditparcer.dagger.component.MainComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

class ApiProvider {
    companion object {
        const val baseUrl = "https://www.reddit.com/"
    }


    fun create(url: String): ApiInterface {
        return mainComponent.getApi()
    }
}