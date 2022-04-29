package id.ac.ukdw.sub1_intermediate

import android.content.Context
import id.ac.ukdw.sub1_intermediate.api.ApiConfig
import id.ac.ukdw.sub1_intermediate.homeStory.StoryDatabase
import id.ac.ukdw.sub1_intermediate.homeStory.StoryRepository

object Injection {
    fun provideRepository( token: String, context: Context): StoryRepository {
        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(token,  database, apiService)
    }
}