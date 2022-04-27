package com.example.storyapp.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.local.StoryDatabase
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.helper.AppExecutors
import com.example.storyapp.helper.LoginSession

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "loginSession")
object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val loginSession = LoginSession.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        val appExecutors = AppExecutors()
        val database = StoryDatabase.getDatabase(context)
        return StoryRepository.getInstance(loginSession, appExecutors, apiService, database)
    }
}