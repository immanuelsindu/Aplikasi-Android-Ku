package id.ac.ukdw.sub1_intermediate

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import id.ac.ukdw.sub1_intermediate.api.ApiConfig
import id.ac.ukdw.sub1_intermediate.homeStory.StoryDatabase
import id.ac.ukdw.sub1_intermediate.homeStory.StoryRepository
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        val myDataStore = UserPreferencesDS.getInstance(context.dataStore)
        return StoryRepository(myDataStore,  database, apiService)
    }
}