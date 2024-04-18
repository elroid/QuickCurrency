package com.elroid.currency.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.elroid.currency.BuildConfig
import com.elroid.currency.data.deserializer.DateTimeSerializer
import com.elroid.currency.data.service.CurrencyService
import com.elroid.currency.data.service.addLoggingInterceptors
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

val dataModule = module {
    single<Json> {
        Json {
            ignoreUnknownKeys = true
            serializersModule = serializersModuleOf(DateTimeSerializer)
        }
    }

    single<OkHttpClient> {
        OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addLoggingInterceptors()
            }
        }.build()
    }
    single<Converter.Factory> {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }
    single<CurrencyService> {
        Retrofit.Builder()
            .baseUrl("https://api.currencyfreaks.com/v2.0/")
            .client(get())
            .addConverterFactory(get<Converter.Factory>())
            .build()
            .create(CurrencyService::class.java)
    }
    single<DataStore<Preferences>> {
        val ctx = get<Context>()
        val prefsName = "user_preferences"
        PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            produceFile = { ctx.preferencesDataStoreFile(prefsName) },
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
        )
    }
}

@Module
@ComponentScan("com.elroid.currency.data")
class AnnotationModule