package com.example.storyapp.ui.stories.add

import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import java.io.File

class AddStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {

    fun uploadImage(image: File?, lat: Double, lon: Double, description: String) = storyRepository
        .uploadImage(image, lat, lon, description)
}