package com.novsu.lampremote

import android.app.Application
import com.novsu.lampremote.di.RemoteAppComponent

class RemoteApp: Application() {
    override fun onCreate() {
        super.onCreate()
        RemoteAppComponent.init(this)
        RemoteAppComponent.provideDependencies()

    }
}