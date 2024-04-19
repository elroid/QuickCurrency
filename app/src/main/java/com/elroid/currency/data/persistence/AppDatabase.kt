package com.elroid.currency.data.persistence

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.elroid.currency.data.mapper.RatesConverters
import com.elroid.currency.data.model.Rates

@Database(
    version = 1,
    entities = [Rates::class],
    exportSchema = true,
)
@TypeConverters(RatesConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun ratesDao(): RatesDao
}
