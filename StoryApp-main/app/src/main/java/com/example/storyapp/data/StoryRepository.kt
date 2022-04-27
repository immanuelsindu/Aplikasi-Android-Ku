package com.example.storyapp.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import com.example.storyapp.data.local.StoryDatabase
import com.example.storyapp.data.local.StoryRemoteMediator
import com.example.storyapp.data.remote.api.ApiConfig
import com.example.storyapp.data.remote.api.ApiService
import com.example.storyapp.data.remote.response.*
import com.example.storyapp.helper.AppExecutors
import com.example.storyapp.helper.LoginSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StoryRepository private constructor(
    private val dataStore: LoginSession,
    private val appExecutors: AppExecutors,
    private val apiService: ApiService,
    private val database: StoryDatabase
) {
    suspend fun clearToken() = dataStore.clearSession()

    @OptIn(ExperimentalPagingApi::class)
    fun getPagingStories(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemoteMediator(database, apiService, getToken()),
            pagingSourceFactory = {
                database.storyDao().getAllStories()
            }
        ).liveData
    }

    fun getStories(page: Int = 1, size: Int = 5, getLocation: Int = 0): ResponseStories {
        return runBlocking {
            apiService.getStories(
                "Bearer ${dataStore.getToken().first()}",
                page,
                size,
                getLocation
            )
        }
    }

    fun login(email: String, password: String): LiveData<Result<Boolean>> {
        val loginResult = MutableLiveData<Result<Boolean>>()

        loginResult.value = Result.Loading
        val service = ApiConfig.getApiService().login(email, password)
        service.enqueue(object : Callback<ResponseLogin> {
            override fun onResponse(call: Call<ResponseLogin>, response: Response<ResponseLogin>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null && !responseBody.error) {
                        runBlocking(Dispatchers.IO) {
                            dataStore.saveToken(responseBody.loginResult.token)
                            dataStore.saveName(responseBody.loginResult.name)
                        }
                        loginResult.value = Result.Success(true)
                    }
                } else {
                    loginResult.value = Result.Error(response.message())
                }
            }

            override fun onFailure(call: Call<ResponseLogin>, t: Throwable) {
                loginResult.value = Result.Error(call.toString())
            }
        })
        return loginResult
    }

    fun registration(name: String, email: String, password: String): LiveData<Result<String>> {
        val registrationResult = MutableLiveData<Result<String>>()

        registrationResult.value = Result.Loading
        val service = ApiConfig.getApiService().register(name, email, password)
        service.enqueue(object : Callback<ResponseRegistration> {
            override fun onResponse(
                call: Call<ResponseRegistration>,
                response: Response<ResponseRegistration>
            ) {
                if (response.isSuccessful){
                    val responseBody = response.body()
                    if (responseBody != null) {
                        registrationResult.value = Result.Success(responseBody.message)
                    }
                }else {
                    val gson = JSONObject(response.errorBody()?.string().toString())
                    val message = gson.getString("message")
                    registrationResult.value = Result.Error(message)
                }
            }

            override fun onFailure(call: Call<ResponseRegistration>, t: Throwable) {
                registrationResult.value = Result.Error("Connection Failure")
            }

        })
        return registrationResult
    }

    fun uploadImage(image: File?, lat: Double, lon: Double, description: String):
            LiveData<Result<String>> {
        val uploadResult = MutableLiveData<Result<String>>()
        uploadResult.value = Result.Loading

        if (image != null) {
            val requestDescription = description.toRequestBody("text/plain".toMediaType())

            val requestImageFile = image.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart = MultipartBody.Part.createFormData(
                "photo",
                image.name,
                requestImageFile
            )

            val service = ApiConfig.getApiService().uploadStory(
                "Bearer ${getToken()}",
                requestDescription, lat, lon, imageMultipart
            )
            service.enqueue(object : Callback<ResponseAddStory> {
                override fun onResponse(
                    call: Call<ResponseAddStory>,
                    response: Response<ResponseAddStory>
                ) {
                    if (response.isSuccessful) {
                        uploadResult.value = Result.Success(response.message())
                    } else {
                        uploadResult.value = Result.Error(response.message())
                    }
                }

                override fun onFailure(call: Call<ResponseAddStory>, t: Throwable) {
                    uploadResult.value = Result.Error("No Internet Connection")
                }

            })
        } else {
            uploadResult.value = Result.Error("Please Select Image")
        }
        return uploadResult
    }

    fun getToken(): String {
        return runBlocking {
            dataStore.getToken().first()
        }
    }

    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            dataStore: LoginSession,
            appExecutors: AppExecutors,
            apiService: ApiService,
            database: StoryDatabase
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(dataStore, appExecutors, apiService, database)
            }.also { instance = it }
    }
}