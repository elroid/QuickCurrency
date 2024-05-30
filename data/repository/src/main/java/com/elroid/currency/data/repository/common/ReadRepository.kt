package com.elroid.currency.data.repository.common


import co.touchlab.kermit.Logger
import com.elroid.currency.core.common.util.nowTs
import com.elroid.currency.core.model.CacheData
import kotlinx.coroutines.flow.Flow
import org.mobilenativefoundation.store.store5.ExperimentalStoreApi
import org.mobilenativefoundation.store.store5.Fetcher
import org.mobilenativefoundation.store.store5.SourceOfTruth
import org.mobilenativefoundation.store.store5.Store
import org.mobilenativefoundation.store.store5.StoreBuilder
import org.mobilenativefoundation.store.store5.StoreReadRequest
import org.mobilenativefoundation.store.store5.StoreReadResponse
import org.mobilenativefoundation.store.store5.impl.extensions.fresh
import org.mobilenativefoundation.store.store5.impl.extensions.get
import java.io.IOException
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

/**
 * An offline capable repository class. Uses local storage as a SOT and offers methods to retrieve based on cached date.
 * Makes use of [Store5's In-Flight debouncer](https://mobilenativefoundation.github.io/Store/store/store/#inflight-debouncer)
 * to ensure repeated requests to the same endpoint before the first request finishes use the result of the first call.
 */
open class ReadRepository<Key : Any, ApiResponseType : Any, DbType : CacheData>(
    private val apiFetcher: suspend (Key) -> ApiResponseType,
    private val networkToLocal: (Key, ApiResponseType) -> DbType,
    private val dbStream: (Key) -> Flow<DbType?>,
    protected val dbInsert: suspend (Key, DbType) -> Unit,
    private val dbDeleteById: suspend (Key) -> Unit,
    private val dbDeleteAll: suspend () -> Unit
) {
    /**
     * Returns a flow of objects, starting with the cached copy (if it exists) followed by a fresh
     * copy from the API. It then passes on any new values that are returned via the `dbStream`
     * function from the Room database.
     */
    fun flowItems(key: Key): Flow<StoreReadResponse<DbType>> =
        itemStore.stream(StoreReadRequest.cached(key, true))

    @Throws(IOException::class)
    suspend fun getItem(key: Key, refresh: Boolean = false): DbType {
        return if (refresh) itemStore.fresh(key) else itemStore.get(key)
    }

    @Throws(IOException::class)
    suspend fun getItem(key: Key, maxAge: Duration, now: Long = nowTs()): DbType {
        val cached = itemStore.get(key)
        val age = (now - cached.timestamp).milliseconds
        return cached.takeIf { age <= maxAge } ?: itemStore.fresh(key)
    }

    /**
     * Specifies room database functions as the source of truth
     */
    private val truthSource: SourceOfTruth<Key, ApiResponseType, DbType> = SourceOfTruth.of(
        reader = { key: Key ->
            dbStream(key)
        },
        writer = { key: Key, apiResponse: ApiResponseType ->
            val localValue = filterNetworkInsert(key, apiResponse)
            dbInsert(key, localValue)
        },
        delete = { key: Key ->
            dbDeleteById(key)
        },
        deleteAll = {
            dbDeleteAll()
        }
    )

    protected open suspend fun filterNetworkInsert(
        key: Key,
        networkValue: ApiResponseType
    ): DbType = networkToLocal(key, networkValue)

    private val itemStore: Store<Key, DbType> = StoreBuilder.from(
        fetcher = Fetcher.of { key ->
            val response = apiFetcher(key)
            Logger.v { "Got response from API:$response" }
            val item = networkToLocal(key, response)
            Logger.v { "Saving item to db:$item" }
            dbInsert(key, item)
            response
        },
        sourceOfTruth = truthSource
    ).build()

    suspend fun delete(key: Key) {
        itemStore.clear(key)
    }

    @OptIn(ExperimentalStoreApi::class)
    suspend fun clear() {
        itemStore.clear()
    }
}