package com.cataract.detection.connection.model

import com.google.gson.annotations.SerializedName

class RegisterModel {
    data class Success(

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("status")
        val status: String? = null
    )

    data class Error(

        @field:SerializedName("message")
        val message: List<String?>? = null,

        @field:SerializedName("status")
        val status: String? = null
    )


}