package id.ac.ukdw.sub1_intermediate

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.ac.ukdw.sub1_intermediate.api.ApiService
import id.ac.ukdw.sub1_intermediate.homeStory.ListStoryItem
import id.ac.ukdw.sub1_intermediate.homeStory.StoryDatabase
import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS
import kotlin.coroutines.coroutineContext
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
//private lateinit var dataStore: DataStore<Preferences>

@OptIn(ExperimentalPagingApi::class)
class StoryRemoteMediator(
    private val token: String,
    private val database: StoryDatabase,
    private val apiService: ApiService
) : RemoteMediator<Int, ListStoryItem>() {

    private companion object {
        const val INITIAL_PAGE_INDEX = 1
        const val BEARER = "Bearer "
    }
    override suspend fun initialize(): RemoteMediator.InitializeAction {
        return RemoteMediator.InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListStoryItem>
    ): RemoteMediator.MediatorResult {

        val token = BEARER + token
        Log.d("StoryRemoteMediator","LAST GAN = $token")
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