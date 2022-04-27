package com.example.storyapp.ui.splash

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository

class SplashViewModel(private val storyRepository: StoryRepository): ViewModel() {
    fun getTokenSession(): String {
        return storyRepository.getToken()
    }
}