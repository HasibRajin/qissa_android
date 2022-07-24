package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class MetaXXXXXXXXXXXX(
    @SerializedName("current_page")
    val currentPage: Any?,
    @SerializedName("first_page")
    val firstPage: Int,
    @SerializedName("first_page_url")
    val firstPageUrl: String,
    @SerializedName("last_page")
    val lastPage: Int,
    @SerializedName("last_page_url")
    val lastPageUrl: String,
    @SerializedName("next_page_url")
    val nextPageUrl: Any?,
    @SerializedName("per_page")
    val perPage: Int,
    @SerializedName("previous_page_url")
    val previousPageUrl: Any?,
    @SerializedName("total")
    val total: Int
)