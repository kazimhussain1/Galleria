package com.example.galleria.home.api.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ImageQueryResponse(
    @SerializedName("hits")
    val imageItems: List<ImageItem>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("totalHits")
    val totalHits: Int
) : Parcelable

