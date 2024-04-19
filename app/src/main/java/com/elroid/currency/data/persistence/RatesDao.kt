package com.elroid.currency.data.persistence

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.elroid.currency.data.model.Rates
import kotlinx.coroutines.flow.Flow

@Dao
interface RatesDao {
    @Query("SELECT * FROM rates where key = :key")
    fun streamRates(key: String): Flow<Rates>

    @Upsert
    suspend fun insert(rates: Rates)

    @Query("DELETE from rates where key = :key")
    suspend fun deleteByKey(key: String)

    @Query("DELETE from rates")
    suspend fun deleteAll()
}
