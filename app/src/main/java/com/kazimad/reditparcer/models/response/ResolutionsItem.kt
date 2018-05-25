package com.kazimad.reditparcer.models.response

import com.google.gson.annotations.SerializedName

data class ResolutionsItem(@SerializedName("width")
                           val width: Int = 0,
                           @SerializedName("url")
                           val url: String = "",
                           @SerializedName("height")
                           val height: Int = 0)