package com.kazimad.reditparcer.models.response

import com.google.gson.annotations.SerializedName

data class TopResponse(@SerializedName("data")
                       val data: Data,
                       @SerializedName("kind")
                       val kind: String = "")