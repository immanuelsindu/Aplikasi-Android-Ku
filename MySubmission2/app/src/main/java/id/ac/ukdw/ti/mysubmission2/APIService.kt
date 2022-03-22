package id.ac.ukdw.ti.mysubmission2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/search/users")
    fun getUsers(@Query("q") q: String): Call<UserResponse>

    @GET("users/{id}")
    fun getUser2(@Path("id") id: String): Call<DetailResponse>

    @GET("users/{id}/followers")
    fun getFollower(@Path("id") id: String): Call<ArrayList<FollowerResponseItem>>

    @GET("users/{id}/following")
    fun getFollowing(@Path("id") id: String): Call<ArrayList<FollowerResponseItem>>
}