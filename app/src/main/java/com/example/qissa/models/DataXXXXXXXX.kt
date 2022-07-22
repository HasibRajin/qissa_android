package com.example.qissa.models


import com.google.gson.annotations.SerializedName

data class DataXXXXXXXX(
    @SerializedName("data")
    val `data`: List<DataXXXXXXXXX>,
    @SerializedName("meta")
    val meta: MetaXXXXX
)