package id.ac.ukdw.sub1_intermediate

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.ac.ukdw.sub1_intermediate.api.ApiService
import id.ac.ukdw.sub1_intermediate.homeStory.ListStoryItem
import id.ac.ukdw.sub1_intermediate.homeStory.StoryDatabase

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val database: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): MediatorResult {
//        val pref = UserPreferencesDS.getInstance()
        val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VySWQiOiJ1c2VyLWFGRGExd3hfRkdFVnJoUHgiLCJpYXQiOjE2NTEyNDY4MjZ9.80vTbK0j6N4thcK66Y4M7WwcZGd4Db1miKbREdJzvvY"
        val page = INITIAL_PAGE_INDEX
        return try {
            val responseData = apiService.getAllStory(token, page, state.config.pageSize)
            val endOfPaginationReached = responseData.isEmpty()
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.storyDao().deleteAll()
                }
                database.storyDao().insertStory(responseData)
            }
            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (exception: Exception) {
            MediatorResult.Error(exception)
        }
    }


}