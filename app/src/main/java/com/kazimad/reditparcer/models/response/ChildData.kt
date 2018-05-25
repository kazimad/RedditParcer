package com.kazimad.reditparcer.models.response

import com.google.gson.annotations.SerializedName

data class ChildData(
        @SerializedName("num_comments")
        val numComments: Int = 0,
        @SerializedName("created_utc")
        val createdUtc: Long = 0,
        @SerializedName("thumbnail")
        val thumbnail: String = "",
        @SerializedName("author")
        val author: String = "",
        @SerializedName("title")
        val title: String = "",
        @SerializedName("url")
        val url: String = "")
