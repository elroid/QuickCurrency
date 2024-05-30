package com.elroid.currency

import android.app.Application
import co.touchlab.kermit.LogcatWriter
import co.touchlab.kermit.Logger
import com.elroid.currency.core.domain.di.domainModule
import com.elroid.currency.data.local.di.localModule
import com.elroid.currency.data.remote.di.remoteModule
import com.elroid.currency.data.repository.di.repositoryModule
import com.elroid.currency.ui.main.featureConverterModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CurrencyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Logger.setLogWriters(LogcatWriter())
        Logger.setTag("QCU")

        startKoin {
            androidContext(this@CurrencyApp)
            modules(
                featureConverterModule,
                domainModule,
                localModule,
                remoteModule,
                repositoryModule,
            )
        }
    }
}