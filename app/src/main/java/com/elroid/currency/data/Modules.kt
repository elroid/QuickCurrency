package com.elroid.currency.data

import com.elroid.currency.data.deserializer.DateTimeSerializer
import com.elroid.currency.data.service.CurrencyService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
        OkHttpClient.Builder().build()
    }
    single<Converter.Factory> {
        get<Json>().asConverterFactory("application/json".toMediaType())
    }
    single<CurrencyService> {
        Retrofit.Builder()
            .baseUrl("https://api.currencyfreaks.com/v2.0")
            .client(get())
            .addConverterFactory(get<Converter.Factory>())
            .build()
            .create(CurrencyService::class.java)
    }
}

@Module
@ComponentScan("com.elroid.currency.data")
class AnnotationModule