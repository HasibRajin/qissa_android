package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class TopicResponse(
    @SerializedName("data")
    val `data`: List<DataXXXXXXXXXXXX>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
