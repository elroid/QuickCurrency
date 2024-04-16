package com.elroid.currency

import android.app.Application
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import com.elroid.currency.data.AnnotationModule
import com.elroid.currency.data.dataModule
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.setLogWriters(LogcatWriter())
        Logger.setTag("QCU")

        startKoin {
            modules(dataModule, AnnotationModule().module)
        }
    }
}