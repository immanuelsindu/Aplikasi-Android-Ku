package com.example.storyapp.ui.registration

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.Result
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.data.remote.response.ResponseRegistration
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel(private val storyRepository: StoryRepository) : ViewModel() {

    fun registration(name: String, email: String, password: String):LiveData<Result<String>> {
        return storyRepository.registration(name, email, password)
    }
}