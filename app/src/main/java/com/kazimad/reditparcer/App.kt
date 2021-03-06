package com.kazimad.reditparcer

import android.app.Application
import com.kazimad.reditparcer.dagger.component.DaggerMainComponent
import com.kazimad.reditparcer.dagger.component.MainComponent
import com.kazimad.reditparcer.dagger.module.ApiModule
import com.kazimad.reditparcer.remote.UsersRepository

class App : Application() {

    companion object {
        lateinit var instance: App
        lateinit var mainComponent: MainComponent
    }

    override fun onCreate() {
        super.onCreate()
        mainComponent = DaggerMainComponent.builder().apiModule(ApiModule()).build()
        instance = this
    }
}