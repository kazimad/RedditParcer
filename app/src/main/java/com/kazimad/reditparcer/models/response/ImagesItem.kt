package com.kazimad.reditparcer.models.response

import com.google.gson.annotations.SerializedName

data class ImagesItem(@SerializedName("resolutions")
                      val resolutions: List<ResolutionsItem>?,
                      @SerializedName("source")
                      val source: Source,
//                      @SerializedName("variants")
//                      val variants: Variants,
                      @SerializedName("id")
                      val id: String = "")