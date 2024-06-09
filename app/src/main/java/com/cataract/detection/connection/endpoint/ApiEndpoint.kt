package com.cataract.detection.connection.endpoint

import com.cataract.detection.connection.model.LoginModel
import com.cataract.detection.connection.model.RegisterModel
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

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
}