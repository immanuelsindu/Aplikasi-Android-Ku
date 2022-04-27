package com.example.storyapp.data.remote.api

import com.example.storyapp.data.remote.response.ResponseAddStory
import com.example.storyapp.data.remote.response.ResponseLogin
import com.example.storyapp.data.remote.response.ResponseRegistration
import com.example.storyapp.data.remote.response.ResponseStories
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("stories")
    suspend fun getStories(
        @Header("Authorization")
        token: String,

        @Query("page")
        page: Int,

        @Query("size")
        size: Int,

        @Query("location")
        location: Int = 0
    ): ResponseStories

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email")
        email: String,

        @Field("password")
        password: String
    ): Call<ResponseLogin>

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name")
        name: String,

        @Field("email")
        email: String,

        @Field("password")
        password: String
    ): Call<ResponseRegistration>

    @Multipart
    @POST("stories")
    fun uploadStory(
        @Header("Authorization")
        token: String,

        @Part("description")
        description: RequestBody,

        @Part("lat")
        lat: Double,

        @Part("lon")
        lon: Double,

        @Part file: MultipartBody.Part
    ): Call<ResponseAddStory>
}