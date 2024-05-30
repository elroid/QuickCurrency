package com.elroid.currency

import com.elroid.currency.core.domain.di.domainModule
import com.elroid.currency.data.local.di.localModule
import com.elroid.currency.data.remote.di.remoteModule
import com.elroid.currency.data.repository.di.repositoryModule
import com.elroid.currency.feature.converter.featureConverterModule

val allModules = listOf(
    featureConverterModule,
    domainModule,
    localModule,
    remoteModule,
    repositoryModule
)