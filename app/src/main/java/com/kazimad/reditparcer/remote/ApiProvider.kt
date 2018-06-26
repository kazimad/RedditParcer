package com.kazimad.reditparcer.remote

import com.google.gson.GsonBuilder
import com.kazimad.reditparcer.BuildConfig
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

        val interceptor = HttpLoggingInterceptor()
        val logLevel = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        val gson = GsonBuilder()
        gson.excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)

        val okHttpClient = OkHttpClient.Builder()
                .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(HttpLoggingInterceptor().setLevel(logLevel))
                .build()

        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(url)
                .client(okHttpClient)
                .build()

        return retrofit.create(ApiInterface::class.java)
    }
}