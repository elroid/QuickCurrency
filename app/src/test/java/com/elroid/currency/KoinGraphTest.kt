package com.elroid.currency

import org.junit.Test
import org.koin.android.test.verify.androidVerify
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import kotlin.reflect.KClass

/**
 * This test aims to spot any missing definitions in the dependency graph. For the most part it works, but due to an
 * issue with how the verify method works (outlined in https://github.com/InsertKoinIO/koin/issues/1501 and 1538)
 * only the constructors are checked, rather than the implementation of the module definition. What this means is that
 * injections of libs which don't use the default constructor (FirebaseAuth for instance) will not fail if omitted
 * from the graph. A question has been submitted (on the issue referenced above) to see if there are any work-arounds.
 */
class KoinGraphTest {

    private val testModule = module {
        includes(allModules)
    }

    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun checkKoinModules() {
        val typesToIgnore = listOf<KClass<*>>()
        testModule.androidVerify(extraTypes = typesToIgnore)
    }
}
