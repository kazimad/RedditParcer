package com.kazimad.reditparcer.dagger.component

import com.kazimad.reditparcer.dagger.module.ApiModule
import com.kazimad.reditparcer.remote.ApiInterface
import com.kazimad.reditparcer.remote.UsersRepository
import dagger.Component
import javax.inject.Inject

@Component(modules = [ApiModule::class])
interface MainComponent {


    fun getApi(): ApiInterface
    fun getUserRepository():UsersRepository
//    fun getGlide(): Glide
//    fun getContext():Context
}