package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class DataXXXXXXXXX(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("followers")
    val followers: List<FollowerX>,
    @SerializedName("id")
    val id: Int,
    @SerializedName("relatable_id")
    val relatableId: Int,
    @SerializedName("relatable_type")
    val relatableType: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user_id")
    val userId: Int
)
