package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class SearchResponse(
    @SerializedName("data")
    val `data`: DataXXXXXXXXXXXXX,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
