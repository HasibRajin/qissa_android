package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class Clap(
    @SerializedName("answer_id")
    val answerId: Int,
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)