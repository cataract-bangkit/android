package com.cataract.detection.connection.model

import com.google.gson.annotations.SerializedName

class DetectionModel {
    data class Success(

        @field:SerializedName("data")
        val data: Data? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("status")
        val status: String? = null
    )

    data class Data(

        @field:SerializedName("result")
        val result: Result? = null
    )

    data class Result(

        @field:SerializedName("result")
        val result: String? = null,

        @field:SerializedName("imgUrl")
        val imgUrl: String? = null,

        @field:SerializedName("predictedAt")
        val predictedAt: String? = null,

        @field:SerializedName("confidence")
        val confidence: Any? = null,

        @field:SerializedName("id")
        val id: String? = null
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