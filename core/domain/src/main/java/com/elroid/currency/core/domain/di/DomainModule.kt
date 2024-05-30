package com.elroid.currency.core.domain.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.ksp.generated.module
@Module
@ComponentScan("com.elroid.currency.core.domain.usecase")
class UseCaseModule

val domainModule = UseCaseModule().module