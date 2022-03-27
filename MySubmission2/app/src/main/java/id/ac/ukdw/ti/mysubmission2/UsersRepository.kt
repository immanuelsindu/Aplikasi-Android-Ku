package id.ac.ukdw.ti.mysubmission2

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import id.ac.ukdw.ti.mysubmission2.data.local.entity.UsersEntity
import id.ac.ukdw.ti.mysubmission2.data.local.room.UsersDao
import id.ac.ukdw.ti.mysubmission2.data.repo.api.ApiService
import id.ac.ukdw.ti.mysubmission2.data.repo.response.UserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersRepository private constructor(
    private val apiService: ApiService,
    private val UserDao: UsersDao,
    private val appExecutors: AppExecutors){

//    private val result = MediatorLiveData<Result<List<UsersEntity>>>()

    fun getFavUser(): LiveData<List<UsersEntity>> = UserDao.getUsers()

    fun insertUsers(user: UsersEntity){
        appExecutors.diskIO.execute {
            UserDao.insertUsers(user)
        }
    }

    fun deleteUsers(user: UsersEntity){
        appExecutors.diskIO.execute {
            UserDao.deleteUsers(user)
        }
    }

    fun isFavorite(login: String) : LiveData<Boolean> = UserDao.isFavourite(login)



    companion object {
        @Volatile
        private var instance: UsersRepository? = null
        fun getInstance(
            apiService: ApiService,
            newsDao: UsersDao,
            appExecutors: AppExecutors
        ): UsersRepository =
            instance ?: synchronized(this) {
                instance ?: UsersRepository(apiService, newsDao, appExecutors)
            }.also { instance = it }
    }
}