package id.ac.ukdw.sub1_intermediate.homeStory

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.ac.ukdw.sub1_intermediate.Injection

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
//
//    val story: LiveData<PagingData<ListStoryItem>> =
//        storyRepository.getStory().cachedIn(viewModelScope)

    fun getAllStories() : LiveData<PagingData<ListStoryItem>> = storyRepository.getStory()

}

class ViewModelFactory(private val token: String, private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(StoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return StoryViewModel(Injection.provideRepository(token,  context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}