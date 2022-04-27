package com.example.storyapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.ui.login.LoginViewModel
import com.example.storyapp.ui.registration.RegistrationViewModel
import com.example.storyapp.ui.splash.SplashViewModel
import com.example.storyapp.ui.stories.ListStoriesViewModel
import com.example.storyapp.ui.stories.add.AddStoryViewModel
import com.example.storyapp.ui.stories.map.MapStoryViewModel

class ViewModelFactory(private val storyRepository: StoryRepository) :
    ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(ListStoriesViewModel::class.java) -> {
                ListStoriesViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> {
                SplashViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(MapStoryViewModel::class.java) -> {
                MapStoryViewModel(storyRepository) as T
            }
            modelClass.isAssignableFrom(RegistrationViewModel::class.java) -> {
                RegistrationViewModel(storyRepository) as T
            }
            else -> throw  IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}