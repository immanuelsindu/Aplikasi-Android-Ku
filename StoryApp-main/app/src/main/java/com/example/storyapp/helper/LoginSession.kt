@file:Suppress("PrivatePropertyName", "PrivatePropertyName")

package com.example.storyapp.helper

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LoginSession private constructor(private val dataStore: DataStore<Preferences>) {
    private val TOKEN_KEY = stringPreferencesKey("session_token")
    private val NAME_KEY = stringPreferencesKey("name")

    fun getToken(): Flow<String> {
        return dataStore.data.map {
            it[TOKEN_KEY] ?: ""
        }
    }

    fun getName(): Flow<String> {
        return dataStore.data.map {
            it[NAME_KEY] ?: ""
        }
    }

    suspend fun saveToken(token: String) {
        dataStore.edit {
            it[TOKEN_KEY] = token
        }
    }

    suspend fun saveName(name: String) {
        dataStore.edit {
            it[NAME_KEY] = name
        }
    }

    suspend fun clearSession() {
        dataStore.edit {
            it.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: LoginSession? = null

        fun getInstance(dataStore: DataStore<Preferences>): LoginSession {
            return INSTANCE ?: synchronized(this) {
                val instance = LoginSession(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}