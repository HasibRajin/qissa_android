package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class SingleUserResponse(
    @SerializedName("data")
    val `data`: List<Data>,
    @SerializedName("message")
    val message: String,
    @SerializedName("success")
    val success: Boolean
)
