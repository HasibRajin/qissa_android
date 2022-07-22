package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class DataXXXXXXXXXXX(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("relatable_id")
    val relatableId: Int,
    @SerializedName("relatable_type")
    val relatableType: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: UserXXX,
    @SerializedName("user_id")
    val userId: Int
)
