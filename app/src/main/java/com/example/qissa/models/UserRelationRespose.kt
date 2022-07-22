package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class UserRelationRespose(
    @SerializedName("data")
    val `data`: DataXXXXXXX,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
