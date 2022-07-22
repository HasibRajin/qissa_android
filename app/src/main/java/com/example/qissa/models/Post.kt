package com.example.qissa.models

import com.google.gson.annotations.SerializedName

data class Post(
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("details")
    val details: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("image")
    val image: Any?,
    @SerializedName("meta")
    val meta: MetaXXXXXXXX,
    @SerializedName("reactions")
    val reactions: List<Reaction>,
    @SerializedName("title")
    val title: String?,
    @SerializedName("topic_id")
    val topicId: Int,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("user")
    val user: UserXXXXX,
    @SerializedName("user_id")
    val userId: Int
)
