package com.kazimad.reditparcer.dagger.component

import android.content.Context
import com.bumptech.glide.Glide
import com.kazimad.reditparcer.dagger.module.ApiModule
import com.kazimad.reditparcer.remote.ApiInterface
import dagger.Component

@Component(modules = [ApiModule::class])
interface MainComponent {
    fun getApi(): ApiInterface
//    fun getGlide(): Glide
//    fun getContext():Context
}