package com.cataract.detection.connection.endpoint

import com.cataract.detection.connection.model.DetectionModel
import com.cataract.detection.connection.model.HistoryModel
import com.cataract.detection.connection.model.LoginModel
import com.cataract.detection.connection.model.RegisterModel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiEndpoint {
    @FormUrlEncoded
    @POST("auth/login")
    fun login(
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<LoginModel.Success>

    @FormUrlEncoded
    @POST("auth/register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<RegisterModel.Success>

    @Multipart
    @POST("predict")
    fun prediction(
        @Part image: MultipartBody.Part,
    ): Call<DetectionModel.Success>

    @GET("predict/histories")
    fun getHistories(): Call<HistoryModel.Success>

}