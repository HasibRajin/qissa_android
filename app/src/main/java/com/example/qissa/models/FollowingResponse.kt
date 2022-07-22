package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class FollowingResponse(
    @SerializedName("data")
    val `data`: DataXXXXXXXXXX,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
