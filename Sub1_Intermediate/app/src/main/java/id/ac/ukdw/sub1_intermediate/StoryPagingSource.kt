package id.ac.ukdw.sub1_intermediate

import androidx.paging.PagingSource
import androidx.paging.PagingState
import id.ac.ukdw.sub1_intermediate.api.ApiService
import id.ac.ukdw.sub1_intermediate.homeStory.ListStoryItem
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS
import kotlinx.coroutines.flow.first

class StoryPagingSource(private val apiService: ApiService) : PagingSource<Int, ListStoryItem>() {
//    private lateinit var mUserPreference: UserPreference
    private lateinit var dataStore: UserPreferencesDS

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val BEARER = "BEARER "
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ListStoryItem> {
        //buat ambil token
//        mUserPreference = UserPreference(this)

//        val pref = UserPreferencesDS.getInstance(dataStore)
//        val userViewModel = ViewModelProvider(this, ViewModelFactory(pref))[UserViewModel::class.java]

        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val token = BEARER + dataStore.getCurrenctToken().first()
            val responseData = apiService.getAllStory(token, position, params.loadSize)
            LoadResult.Page(
                data = responseData,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ListStoryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

}