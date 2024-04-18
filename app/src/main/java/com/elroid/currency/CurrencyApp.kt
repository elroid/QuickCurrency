package com.elroid.currency

import android.app.Application
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import com.elroid.currency.data.AnnotationModule
import com.elroid.currency.data.dataModule
import com.elroid.currency.domain.UseCaseModule
import com.elroid.currency.ui.common.UiModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.setLogWriters(LogcatWriter())
        Logger.setTag("QCU")

        startKoin {
            androidContext(this@CurrencyApp)
            modules(
                dataModule,
                AnnotationModule().module,
                UiModule().module,
                UseCaseModule().module,
            )
        }
    }
}