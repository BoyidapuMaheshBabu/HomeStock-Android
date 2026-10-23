package com.boyidapu.homestock.app

import android.app.Application

class HomeStockApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}
