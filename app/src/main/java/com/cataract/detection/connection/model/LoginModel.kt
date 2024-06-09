package com.cataract.detection.connection.model

import com.google.gson.annotations.SerializedName

class LoginModel {
    data class Success(

        @field:SerializedName("data")
        val data: Data? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("status")
        val status: String? = null
    )

    data class Data(

        @field:SerializedName("token")
        val token: String? = null
    )

    data class Error(

        @field:SerializedName("code")
        val code: String? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("status")
        val status: String? = null
    )
}