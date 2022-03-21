package id.ac.ukdw.ti.mysubmission2

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/search/users")
    fun getUsers(@Query("q") q: String): Call<UserResponse>
}