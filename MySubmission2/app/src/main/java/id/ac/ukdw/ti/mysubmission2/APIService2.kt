package id.ac.ukdw.ti.mysubmission2

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface APIService2 {
    @GET("users/{id}")
    fun getUser2(@Path("id") id: String): Call<DetailResponse>
}