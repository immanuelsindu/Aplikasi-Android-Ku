package com.example.storyapp.ui.stories

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.data.StoryRepository
import com.example.storyapp.data.remote.response.ListStoryItem
import kotlinx.coroutines.launch

class ListStoriesViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    fun clearSession() {
        viewModelScope.launch {
            storyRepository.clearToken()
        }
    }

    fun findStories(): LiveData<PagingData<ListStoryItem>> =
        storyRepository.getPagingStories()
}