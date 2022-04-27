package com.example.storyapp.ui.stories.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.response.ListStoryItem

class MapStoryViewModel(private val storyRepository: StoryRepository): ViewModel() {
    private val _newStories =  MutableLiveData<ArrayList<ListStoryItem>>()
    val newStories: LiveData<ArrayList<ListStoryItem>> = _newStories

    fun getNewStories(page: Int, size: Int = 5) {
        _newStories.value = storyRepository.getStories(page, size, 1).listStory
    }
}