package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("data")
    val `data`: DataXXXXXXXXXXXXXX,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)