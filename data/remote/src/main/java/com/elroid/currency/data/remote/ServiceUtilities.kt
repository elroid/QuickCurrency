package com.elroid.currency.data.remote

import co.touchlab.kermit.Logger
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Divides log content - BASIC info will go to DEBUG while all other BODY content will be VERBOSE only
 */
fun OkHttpClient.Builder.addLoggingInterceptors() {
    val logger = Logger.withTag("QCU-OkHttp")
    val debugLogger = HttpLoggingInterceptor.Logger { logger.d(it) }
    addInterceptor(HttpLoggingInterceptor(debugLogger).setLevel(HttpLoggingInterceptor.Level.BASIC))
    val verboseLogger = HttpLoggingInterceptor.Logger { if (!it.isBasic()) logger.v(it) }
    addInterceptor(HttpLoggingInterceptor(verboseLogger).setLevel(HttpLoggingInterceptor.Level.BODY))
}

private fun String.isBasic(): Boolean = startsWith("<--") || startsWith("-->")