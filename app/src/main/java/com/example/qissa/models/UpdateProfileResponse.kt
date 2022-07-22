package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(
    @SerializedName("data")
    val `data`: DataXXXXXX,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
