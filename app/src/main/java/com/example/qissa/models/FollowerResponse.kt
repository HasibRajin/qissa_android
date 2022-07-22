package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class FollowerResponse(
    @SerializedName("data")
    val `data`: DataXXXXXXXX,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)