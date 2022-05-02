package id.ac.ukdw.sub1_intermediate.homeStory

import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import id.ac.ukdw.sub1_intermediate.StoryRemoteMediator
import id.ac.ukdw.sub1_intermediate.api.ApiService

import id.ac.ukdw.sub1_intermediate.userSession.UserPreferencesDS
import kotlinx.coroutines.runBlocking

//private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user")
class StoryRepository(private val myDataStore: UserPreferencesDS, private val storyDatabase: StoryDatabase, private val apiService: ApiService) {
    @OptIn(ExperimentalPagingApi::class)
    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        Log.d("StoryRepository", "Ini TOKEN = " + "Bearer " + getToken())
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(storyDatabase, apiService,  "Bearer " + getToken()),
            pagingSourceFactory = {
//                QuotePagingSource(apiService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData

    }

    fun getMapListStory(page: Int = 1, size: Int = 5) : GetAllStoryResponse {
        return runBlocking {
            apiService.getAllStory("Bearer " + getToken(), page, size)
        }
    }

    private fun getToken() : String{
        return runBlocking {
           myDataStore.getCurrenctToken()!!
//            Log.d("StoryRepository", "TOKEN GAN =  " +" Bearer " + myDataStore.getCurrenctToken().toString())
        }

    }
}