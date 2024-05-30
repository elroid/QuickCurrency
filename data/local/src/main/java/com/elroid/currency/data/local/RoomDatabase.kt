package com.elroid.currency.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    version = 1,
    entities = [LocalRates::class],
    exportSchema = true,
)
@TypeConverters(RatesConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
}