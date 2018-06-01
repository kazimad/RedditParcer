package com.kazimad.reditparcer

import android.app.Application
import com.kazimad.reditparcer.remote.Repository

class App : Application() {

    companion object {
        lateinit var instance: App
        lateinit var repository: Repository
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        repository = Repository()
    }
}