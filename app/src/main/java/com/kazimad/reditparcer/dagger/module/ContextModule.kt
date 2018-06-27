package com.kazimad.reditparcer.dagger.module

import android.content.Context
import dagger.Module
import dagger.Provides



@Module
class ContextModule {
    lateinit var mContext: Context

    fun ContextModule(context: Context){
        this.mContext = context
    }

    @Provides
    fun getmContext(): Context {
        return mContext.getApplicationContext()
    }
}