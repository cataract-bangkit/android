package com.cataract.detection.connection.model

import com.google.gson.annotations.SerializedName

class HistoryModel {
    data class Success(

        @field:SerializedName("data")
        val data: Data? = null,

        @field:SerializedName("message")
        val message: String? = null,

        @field:SerializedName("status")
        val status: String? = null
    )

    data class HistoryItem(

        @field:SerializedName("result")
        val result: String? = null,

        @field:SerializedName("imgUrl")
        val imgUrl: String? = null,

        @field:SerializedName("predictedAt")
        val predictedAt: String? = null,

        @field:SerializedName("confidence")
        val confidence: String? = null,

        @field:SerializedName("id")
        val id: String? = null
    )

    data class Data(

        @field:SerializedName("history")
        val history: List<HistoryItem?>? = null
    )
}