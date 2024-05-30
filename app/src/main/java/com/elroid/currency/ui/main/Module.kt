package com.elroid.currency.ui.main

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.elroid.currency.ui.main")
class FeatureConverterModule

val featureConverterModule = module {
    includes(FeatureConverterModule().module)
}