package com.kazimad.reditparcer.models.response

import com.google.gson.annotations.SerializedName

data class ChildrenItem(@SerializedName("data")
                        val data: ChildData,
                        @SerializedName("kind")
                        val kind: String = "")