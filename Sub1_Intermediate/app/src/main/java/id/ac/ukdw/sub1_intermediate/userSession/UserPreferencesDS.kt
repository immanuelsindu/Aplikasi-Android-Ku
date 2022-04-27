package id.ac.ukdw.sub1_intermediate.userSession

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferencesDS private constructor(private val dataStore: DataStore<Preferences>) {
    private val CURRENT_TOKEN = stringPreferencesKey("current_token")

    suspend fun saveCurrentToken(mUserToken: String){
        dataStore.edit{
            it[CURRENT_TOKEN] = mUserToken
        }
    }

    fun getCurrenctToken(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[CURRENT_TOKEN] ?: ""
        }
    }


    companion object {
        @Volatile
        private var INSTANCE: UserPreferencesDS? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferencesDS {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferencesDS(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}