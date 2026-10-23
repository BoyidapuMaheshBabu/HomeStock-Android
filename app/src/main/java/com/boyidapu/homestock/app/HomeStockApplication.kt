package com.boyidapu.homestock.app

import android.app.Application

class HomeStockApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
