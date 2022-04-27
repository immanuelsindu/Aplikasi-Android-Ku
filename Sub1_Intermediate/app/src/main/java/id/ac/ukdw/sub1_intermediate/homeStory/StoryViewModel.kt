package id.ac.ukdw.sub1_intermediate.homeStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn

class StoryViewModel(storyRepository: StoryRepository) : ViewModel() {

    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)



}