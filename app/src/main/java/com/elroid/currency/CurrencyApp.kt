package com.elroid.currency

import android.app.Application
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.setLogWriters(LogcatWriter())
        Logger.setTag("QCU")

        startKoin {
            androidContext(this@CurrencyApp)
            modules(allModules)
        }
    }
}