package com.elroid.currency.data.remote.di

import com.elroid.currency.data.remote.BuildConfig
import com.elroid.currency.data.remote.CurrencyService
import com.elroid.currency.data.remote.DateTimeSerializer
import com.elroid.currency.data.remote.addLoggingInterceptors
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.serializersModuleOf
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import org.koin.dsl.module
import retrofit2.Converter
import retrofit2.Retrofit

val remoteModule = module {
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
}