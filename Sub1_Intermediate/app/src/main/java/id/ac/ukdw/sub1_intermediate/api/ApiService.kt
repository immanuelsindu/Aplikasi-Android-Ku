package id.ac.ukdw.sub1_intermediate.api

import id.ac.ukdw.sub1_intermediate.homeStory.GetAllStoryResponse
import id.ac.ukdw.sub1_intermediate.login.LoginResponse
import id.ac.ukdw.sub1_intermediate.newStory.GuestUploadResponse
import id.ac.ukdw.sub1_intermediate.newStory.UploadStoryResponse
import id.ac.ukdw.sub1_intermediate.register.RegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<UploadStoryResponse>

    @GET("stories")
    fun getAllStory(@Header("Authorization") token: String): Call<GetAllStoryResponse>

    @Multipart
    @POST("stories/guest")
    fun uploadImageGuest(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody
    ): Call<GuestUploadResponse>
}