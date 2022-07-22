package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class MetaXXXXXXXX(
    @SerializedName("comments_count")
    val commentsCount: String,
    @SerializedName("reactions_count")
    val reactionsCount: String
)