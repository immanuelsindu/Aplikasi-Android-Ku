package id.ac.ukdw.ti.mysubmission2

import android.content.Context
import id.ac.ukdw.ti.mysubmission2.data.local.room.UsersDatabase
import id.ac.ukdw.ti.mysubmission2.data.repo.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): UsersRepository {
        val apiService = ApiConfig.getApiService()
        val database = UsersDatabase.getInstance(context)
        val dao = database.newsDao()
        val appExecutors = AppExecutors()
        return UsersRepository.getInstance(apiService, dao, appExecutors)
    }
}