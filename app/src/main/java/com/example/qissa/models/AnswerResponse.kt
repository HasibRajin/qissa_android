package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class AnswerResponse(
    @SerializedName("data")
    val `data`: List<DataXXXXXXXXXXXXXXXX>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)