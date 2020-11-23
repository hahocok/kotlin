package com.android.kotlin

import android.app.Application
import com.android.kotlin.di.appModule
import com.android.kotlin.di.splashModule
import com.android.kotlin.di.mainModule
import com.android.kotlin.di.noteModule
import org.koin.android.ext.android.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule, splashModule, mainModule, noteModule))
    }
}