package id.ac.ukdw.sub1_intermediate.homeStory

import androidx.lifecycle.LiveData
import androidx.paging.*
import id.ac.ukdw.sub1_intermediate.StoryRemoteMediator
import id.ac.ukdw.sub1_intermediate.api.ApiService


class StoryRepository(private val token: String, private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(token, storyDatabase, apiService),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }



}