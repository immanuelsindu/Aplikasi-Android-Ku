package com.example.storyapp.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Result
import com.example.storyapp.data.StoryRepository

class LoginViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun login(email: String, password: String): LiveData<Result<Boolean>> {
        return storyRepository.login(email, password)
    }
}