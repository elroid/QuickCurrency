package com.elroid.currency.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {
    @Query("SELECT * FROM rates where key = :key")
    fun streamRates(key: String): Flow<LocalRates>

    @Upsert
    suspend fun insert(rates: LocalRates)

    @Query("DELETE from rates where key = :key")
    suspend fun deleteByKey(key: String)

    @Query("DELETE from rates")
    suspend fun deleteAll()
}