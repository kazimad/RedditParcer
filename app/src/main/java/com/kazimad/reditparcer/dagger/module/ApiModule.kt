package com.kazimad.reditparcer.dagger.module

import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kazimad.reditparcer.BuildConfig
import com.kazimad.reditparcer.remote.ApiInterface
import com.kazimad.reditparcer.remote.ApiRepository
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Modifier
import java.util.concurrent.TimeUnit

@Module
class ApiModule {

    @Provides
    fun getInterceptor() = HttpLoggingInterceptor()

    @Provides
    fun getLogLevel() = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

    @Provides
    fun getGson(): Gson {
        return GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithModifiers(Modifier.FINAL, Modifier.TRANSIENT, Modifier.STATIC)
                .create()
    }

    @Provides
    fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
//                .addNetworkInterceptor(getInterceptor()) // same for .addInterceptor(...)
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addNetworkInterceptor(getInterceptor().setLevel(getLogLevel()))
                .build()
    }

    @Provides
    fun provideApi(): ApiInterface {
        val retrofit = Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiRepository.baseUrl)
                .client(getOkHttpClient())
                .build()

        return retrofit.create(ApiInterface::class.java)
    }
}