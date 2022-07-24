package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class DataXXXXXXXXXXXXXXX(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("meta")
    val meta: MetaXXXXXXXXXX,
    @SerializedName("title")
    val title: String,
    @SerializedName("topic_id")
    val topicId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: UserXXXX,
    @SerializedName("user_id")
    val userId: Int,
    var flag: Boolean = false
)
