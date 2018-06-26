package com.kazimad.reditparcer.dagger.component

import com.bumptech.glide.Glide
import com.kazimad.reditparcer.remote.ApiInterface
import dagger.Component

@Component
interface MainComponent {
    fun getApi(): ApiInterface
    fun getGlide(): Glide
}