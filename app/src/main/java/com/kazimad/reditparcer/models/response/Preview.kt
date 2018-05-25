package com.kazimad.reditparcer.models.response

import com.google.gson.annotations.SerializedName

data class Preview(@SerializedName("images")
                   val images: List<ImagesItem>?,
                   @SerializedName("enabled")
                   val enabled: Boolean = false)