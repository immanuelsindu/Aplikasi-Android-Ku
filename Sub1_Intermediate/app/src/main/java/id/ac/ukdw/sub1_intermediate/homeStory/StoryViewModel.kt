package id.ac.ukdw.sub1_intermediate.homeStory

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import id.ac.ukdw.sub1_intermediate.Injection

class StoryViewModel(private val storyRepository: StoryRepository) : ViewModel() {
    private val mapsListStoryOBS = MutableLiveData <ArrayList<ListStoryItem>>()
    internal val mapsListStory : LiveData<ArrayList<ListStoryItem>> = mapsListStoryOBS

    fun getMapListStory(page: Int = 1, size: Int = 5){
        mapsListStoryOBS.value = storyRepository.getMapListStory(page, size).listStory
    }
    fun getAllStories() : LiveData<PagingData<ListStoryItem>> = storyRepository.getStory()


    val story: LiveData<PagingData<ListStoryItem>> =
        storyRepository.getStory().cachedIn(viewModelScope)

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