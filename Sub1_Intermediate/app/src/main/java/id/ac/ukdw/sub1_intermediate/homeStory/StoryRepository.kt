package id.ac.ukdw.sub1_intermediate.homeStory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import id.ac.ukdw.sub1_intermediate.StoryRemoteMediator
import id.ac.ukdw.sub1_intermediate.api.ApiService
import kotlinx.coroutines.runBlocking


class StoryRepository(private val token: String, private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService, token),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }

    fun getMapListStory(page: Int = 1, size: Int = 5) : GetAllStoryResponse {
        return runBlocking {
            apiService.getAllStory(token, page, size)
        }
    }
}