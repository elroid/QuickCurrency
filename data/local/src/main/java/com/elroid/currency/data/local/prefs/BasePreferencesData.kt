package com.elroid.currency.data.local.prefs


import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import co.touchlab.kermit.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import java.io.IOException
import kotlin.reflect.KType

/**
 * Provides gettable/flowable access to primitives and Serializable classes. To be used as a replacement for
 * SharedPreferences and a lightweight alternative to a room database for single-instance classes.
 */
@Suppress("unused", "SameParameterValue")
open class BasePreferencesData(
    private val dataStore: DataStore<Preferences>,
    private val json: Json,
) {
    inner class NullableJsonPref<T>(
        name: String,
        type: KType,
        private val mapIn: (T) -> String = { encodeToString(type, it) },
        private val mapOut: (String) -> T? = { decodeFromString(type, it) },
    ) {
        private val stringPref = createStringPref(name)

        suspend fun setValue(value: T?) {
            stringPref.setValue(value?.let { mapIn(it) })
        }

        fun setValueBlocking(value: T?) = runBlocking { setValue(value) }

        /**
         * Gets property, applies transform, and saves
         */
        suspend fun updateValue(transform: T?.() -> T) {
            stringPref.updateValue {
                val currentValue = this?.let { mapOut(it) }
                val newValue = transform(currentValue)
                mapIn(newValue)
            }
        }

        suspend fun clearValue() {
            stringPref.clearValue()
        }

        val value: T? get() = stringPref.value?.let { mapOut(it) }

        fun flow(): Flow<T?> = stringPref.flow().map { str -> str?.let { mapOut(it) } }
    }

    inner class JsonPref<T>(
        name: String,
        type: KType,
        private val defaultValue: T,
        private val mapIn: (T) -> String = { encodeToString(type, it) },
        private val mapOut: (String) -> T = { decodeFromString(type, it) ?: defaultValue },
    ) {
        private val stringPref = createStringPref(name)

        suspend fun setValue(value: T?) {
            stringPref.setValue(mapIn(value ?: defaultValue))
        }

        fun setValueBlocking(value: T?) = runBlocking { setValue(value) }

        suspend fun updateValue(transform: T.() -> T) {
            stringPref.updateValue {
                val currentValue = this?.let { mapOut(it) } ?: defaultValue
                val newValue = currentValue.transform()
                mapIn(newValue)
            }
        }

        fun updateValueBlocking(transform: T.() -> T) = runBlocking { updateValue(transform) }

        suspend fun clearValue() {
            stringPref.clearValue()
        }

        val value: T get() = stringPref.value?.let { mapOut(it) } ?: defaultValue

        fun flow(): Flow<T> = stringPref.flow().map { str -> str?.let { mapOut(it) } ?: defaultValue }
    }

    private fun <I> encodeToString(type: KType, model: I): String {
        return json.encodeToString(serializer(type), model)
    }

    private fun <I> decodeFromString(type: KType, str: String): I? {
        @Suppress("UNCHECKED_CAST") // No way to avoid this
        return json.decodeFromString(serializer(type), str) as I?
    }

    inner class NullablePref<T>(type: () -> Preferences.Key<T>) {
        private val key = type()

        suspend fun setValue(value: T?) {
            dataStore.edit { preferences ->
                value?.let { preferences[key] = it } ?: run { preferences.remove(key) }
            }
        }

        fun setValueBlocking(value: T?) = runBlocking { setValue(value) }

        /**
         * Uses the atomic nature of dataStore.edit to update the value in a thread-safe way.
         */
        suspend fun updateValue(transform: T?.() -> T) {
            dataStore.edit { preferences ->
                val currentValue = preferences[key]
                preferences[key] = currentValue.transform()
            }
        }

        fun updateValueBlocking(transform: T?.() -> T) = runBlocking { updateValue(transform) }

        suspend fun clearValue() {
            setValue(null)
        }

        val value: T?
            get() = runBlocking {
                try {
                    dataStore.data.first().let { it[key] }
                } catch (e: NoSuchElementException) {
                    Logger.d(e) { "$this.value was not found" }
                    null
                } catch (e: Exception) {
                    Logger.w(e) { "Reading $this.value($value) resulted in an error (encryption related?)" }
                    null
                }
            }

        fun flow(): Flow<T?> = dataStore.data
            .catch { exception ->
                Logger.i(exception) { "Error reading $key" }
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else throw exception
            }
            .map { it[key] }
            .distinctUntilChanged()
    }

    inner class SimplePref<T>(
        private val defaultValue: T,
        private val type: () -> Preferences.Key<T>
    ) {
        private val nullablePref = NullablePref(type)

        suspend fun setValue(value: T) {
            nullablePref.setValue(value)
        }

        fun setValueBlocking(value: T) = runBlocking { setValue(value) }

        suspend fun updateValue(transform: T.() -> T) {
            dataStore.edit { preferences ->
                val key = type()
                val currentValue = preferences[key] ?: defaultValue
                preferences[key] = currentValue.transform()
            }
        }

        fun updateValueBlocking(transform: T?.() -> T) = runBlocking { updateValue(transform) }

        suspend fun clearValue() {
            nullablePref.clearValue()
        }

        val value: T get() = nullablePref.value ?: defaultValue

        fun flow(): Flow<T> = nullablePref.flow().map { it ?: defaultValue }
    }

    protected fun <T> createJsonPref(name: String, type: KType) =
        NullableJsonPref<T>(name, type)

    protected fun <T> createJsonPref(name: String, type: KType, defaultValue: T) =
        JsonPref(name, type, defaultValue)

    protected fun createBooleanPref(name: String) =
        NullablePref { booleanPreferencesKey(name) }

    protected fun createBooleanPref(name: String, defaultValue: Boolean) =
        SimplePref(defaultValue) { booleanPreferencesKey(name) }

    protected fun createDoublePref(name: String) =
        NullablePref { doublePreferencesKey(name) }

    protected fun createDoublePref(name: String, defaultValue: Double) =
        SimplePref(defaultValue) { doublePreferencesKey(name) }

    protected fun createFloatPref(name: String) =
        NullablePref { floatPreferencesKey(name) }

    protected fun createFloatPref(name: String, defaultValue: Float) =
        SimplePref(defaultValue) { floatPreferencesKey(name) }

    protected fun createIntPref(name: String) =
        NullablePref { intPreferencesKey(name) }

    protected fun createIntPref(name: String, defaultValue: Int) =
        SimplePref(defaultValue) { intPreferencesKey(name) }

    protected fun createLongPref(name: String) =
        NullablePref { doublePreferencesKey(name) }

    protected fun createLongPref(name: String, defaultValue: Long) =
        SimplePref(defaultValue) { longPreferencesKey(name) }

    protected fun createStringPref(name: String) =
        NullablePref { stringPreferencesKey(name) }

    protected fun createStringPref(name: String, defaultValue: String) =
        SimplePref(defaultValue) { stringPreferencesKey(name) }
}