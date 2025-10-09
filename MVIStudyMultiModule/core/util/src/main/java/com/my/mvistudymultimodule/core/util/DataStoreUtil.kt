package com.my.mvistudymultimodule.core.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object DataStoreUtil {

    private const val DATASTORE_NAME = "app_datastore"
    @PublishedApi
    internal val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_NAME)

    @PublishedApi
    internal val gson = Gson()

    // ======================== String ========================
    suspend fun setString(context: Context, key: String, value: String) {
        val prefKey = stringPreferencesKey(key)
        val encrypted = SecureKeyManager.encrypt(context, value)
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend fun getString(context: Context, key: String, defaultValue: String = ""): String {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return defaultValue
        return try {
            SecureKeyManager.decrypt(context, encrypted)
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getStringFlow(context: Context, key: String, defaultValue: String = ""): Flow<String> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map defaultValue
            try { SecureKeyManager.decrypt(context, encrypted) } catch (e: Exception) { defaultValue }
        }
    }

    // ======================== Int ========================
    suspend fun setInt(context: Context, key: String, value: Int) {
        val prefKey = stringPreferencesKey(key)
        val encrypted = SecureKeyManager.encrypt(context, value.toString())
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend fun getInt(context: Context, key: String, defaultValue: Int = 0): Int {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return defaultValue
        return try {
            SecureKeyManager.decrypt(context, encrypted).toIntOrNull() ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getIntFlow(context: Context, key: String, defaultValue: Int = 0): Flow<Int> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map defaultValue
            try { SecureKeyManager.decrypt(context, encrypted).toIntOrNull() ?: defaultValue }
            catch (e: Exception) { defaultValue }
        }
    }

    // ======================== Long ========================
    suspend fun setLong(context: Context, key: String, value: Long) {
        val prefKey = stringPreferencesKey(key)
        val encrypted = SecureKeyManager.encrypt(context, value.toString())
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend fun getLong(context: Context, key: String, defaultValue: Long = 0L): Long {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return defaultValue
        return try {
            SecureKeyManager.decrypt(context, encrypted).toLongOrNull() ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getLongFlow(context: Context, key: String, defaultValue: Long = 0L): Flow<Long> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map defaultValue
            try { SecureKeyManager.decrypt(context, encrypted).toLongOrNull() ?: defaultValue }
            catch (e: Exception) { defaultValue }
        }
    }

    // ======================== Float ========================
    suspend fun setFloat(context: Context, key: String, value: Float) {
        val prefKey = stringPreferencesKey(key)
        val encrypted = SecureKeyManager.encrypt(context, value.toString())
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend fun getFloat(context: Context, key: String, defaultValue: Float = 0f): Float {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return defaultValue
        return try {
            SecureKeyManager.decrypt(context, encrypted).toFloatOrNull() ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getFloatFlow(context: Context, key: String, defaultValue: Float = 0f): Flow<Float> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map defaultValue
            try { SecureKeyManager.decrypt(context, encrypted).toFloatOrNull() ?: defaultValue }
            catch (e: Exception) { defaultValue }
        }
    }

    // ======================== Double ========================
    suspend fun setDouble(context: Context, key: String, value: Double) {
        val prefKey = stringPreferencesKey(key)
        val encrypted = SecureKeyManager.encrypt(context, value.toString())
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend fun getDouble(context: Context, key: String, defaultValue: Double = 0.0): Double {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return defaultValue
        return try {
            SecureKeyManager.decrypt(context, encrypted).toDoubleOrNull() ?: defaultValue
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getDoubleFlow(context: Context, key: String, defaultValue: Double = 0.0): Flow<Double> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map defaultValue
            try { SecureKeyManager.decrypt(context, encrypted).toDoubleOrNull() ?: defaultValue }
            catch (e: Exception) { defaultValue }
        }
    }

    // ======================== Boolean ========================
    suspend fun setBoolean(context: Context, key: String, value: Boolean) {
        val prefKey = stringPreferencesKey(key)
        val encrypted = SecureKeyManager.encrypt(context, value.toString())
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend fun getBoolean(context: Context, key: String, defaultValue: Boolean = false): Boolean {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return defaultValue
        return try {
            SecureKeyManager.decrypt(context, encrypted).toBoolean()
        } catch (e: Exception) {
            defaultValue
        }
    }

    fun getBooleanFlow(context: Context, key: String, defaultValue: Boolean = false): Flow<Boolean> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map defaultValue
            try { SecureKeyManager.decrypt(context, encrypted).toBoolean() } catch (e: Exception) { defaultValue }
        }
    }

    // ======================== JSON Object ========================
    suspend fun <T> setObject(context: Context, key: String, value: T) {
        val prefKey = stringPreferencesKey(key)
        val jsonString = gson.toJson(value)
        val encrypted = SecureKeyManager.encrypt(context, jsonString)
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend inline fun <reified T> getObject(context: Context, key: String): T? {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return null
        return try {
            val json = SecureKeyManager.decrypt(context, encrypted)
            gson.fromJson<T>(json, object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T> getObjectFlow(context: Context, key: String): Flow<T?> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map null
            try {
                val json = SecureKeyManager.decrypt(context, encrypted)
                gson.fromJson<T>(json, object : TypeToken<T>() {}.type)
            } catch (e: Exception) { null }
        }
    }

    // ======================== JSON List ========================
    suspend fun <T> setList(context: Context, key: String, value: List<T>) {
        val prefKey = stringPreferencesKey(key)
        val jsonString = gson.toJson(value)
        val encrypted = SecureKeyManager.encrypt(context, jsonString)
        context.dataStore.edit { it[prefKey] = encrypted }
    }

    suspend inline fun <reified T> getList(context: Context, key: String): List<T>? {
        val prefKey = stringPreferencesKey(key)
        val encrypted = context.dataStore.data.map { it[prefKey] }.first() ?: return null
        return try {
            val json = SecureKeyManager.decrypt(context, encrypted)
            gson.fromJson<List<T>>(json, object : TypeToken<List<T>>() {}.type)
        } catch (e: Exception) {
            null
        }
    }

    inline fun <reified T> getListFlow(context: Context, key: String): Flow<List<T>?> {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { prefs ->
            val encrypted = prefs[prefKey] ?: return@map null
            try {
                val json = SecureKeyManager.decrypt(context, encrypted)
                gson.fromJson<List<T>>(json, object : TypeToken<List<T>>() {}.type)
            } catch (e: Exception) { null }
        }
    }

    // ======================== Utility ========================
    suspend fun remove(context: Context, key: String) {
        val prefKey = stringPreferencesKey(key)
        context.dataStore.edit { it.remove(prefKey) }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit { it.clear() }
    }

    suspend fun contains(context: Context, key: String): Boolean {
        val prefKey = stringPreferencesKey(key)
        return context.dataStore.data.map { it.contains(prefKey) }.first()
    }

    suspend fun getAllData(context: Context): Map<String, Any?> {
        return context.dataStore.data.map { prefs ->
            prefs.asMap().mapKeys { it.key.name }
        }.first()
    }
}