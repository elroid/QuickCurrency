package com.elroid.currency.data.local.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.elroid.currency.data.local.AppDatabase
import com.elroid.currency.data.local.RatesDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.ksp.generated.module

@Module
@ComponentScan("com.elroid.currency.data.local.prefs")
class PrefsModule

val localModule = module {
    includes(PrefsModule().module)
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
    single<AppDatabase> {
        Room.databaseBuilder(get(), AppDatabase::class.java, "quickCurrency")
            .fallbackToDestructiveMigration()
            .build()
    }
    single<RatesDao> { get<AppDatabase>().ratesDao() }
}