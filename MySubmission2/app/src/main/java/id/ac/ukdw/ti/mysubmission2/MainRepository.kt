package id.ac.ukdw.ti.mysubmission2

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainRepository private constructor(
    private val apiService: ApiService,
    private val newsDao: UsersDao,
    private val appExecutors: AppExecutors){

    private val result = MediatorLiveData<Result<List<UsersEntity>>>()

    //findUser()
    fun finUsers(keywordUser: String): LiveData<Result<List<UsersEntity>>> {
        result.value = Result.Loading
        //val client = apiService.getNews(BuildConfig.API_KEY)
        val client = apiService.getUsers(keywordUser)
        client.enqueue(object : Callback<UserResponse> {
            override fun onResponse(call: Call<UserResponse>, response: Response<UserResponse>) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
//                        showRecyclerList(responseBody.items
                        val users = responseBody.items
                        val newsList = ArrayList<UsersEntity>()
                        appExecutors.diskIO.execute {
                            users?.forEach { users ->
                                val isBookmarked = newsDao.isNewsBookmarked(users.login)
                                val news = UsersEntity(
                                    users.login,
                                    users.avatarUrl,
                                    isBookmarked
                                )
                                newsList.add(news)
                            }
                            newsDao.deleteAll()
                            newsDao.insertUsers(newsList)
                        }
                    }
//                    val articles = response.body()?.articles
//                    val newsList = ArrayList<NewsEntity>()
//                    appExecutors.diskIO.execute {
//                        articles?.forEach { article ->
//                            val isBookmarked = newsDao.isNewsBookmarked(article.title)
//                            val news = UsersEntity(
//                                article.title,
//                                article.publishedAt,
//                                article.urlToImage,
//                                article.url,
//                                isBookmarked
//                            )
//                            newsList.add(news)
//                        }
//                        newsDao.deleteAll()
//                        newsDao.insertNews(newsList)
//                    }
                }
            }
            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                result.value = Result.Error(t.message.toString())
            }
        })
        val localData = newsDao.getUsers()
        result.addSource(localData) { newData: List<UsersEntity> ->
            result.value = Result.Success(newData)
        }
        return result
    }

    companion object {
        @Volatile
        private var instance: MainRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: UsersDao,
            appExecutors: AppExecutors
        ): MainRepository =
            instance ?: synchronized(this) {
                instance ?: MainRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}