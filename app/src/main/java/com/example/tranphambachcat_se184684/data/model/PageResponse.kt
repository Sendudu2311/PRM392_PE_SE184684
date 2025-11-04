package com.example.tranphambachcat_se184684.data.model

import com.google.gson.annotations.SerializedName

/**
 * Paginated API response wrapper
 */
data class PageResponse<T>(
    @SerializedName("items")
    val items: List<T>,

    @SerializedName("page")
    val page: Int,

    @SerializedName("size")
    val size: Int,

    @SerializedName("total")
    val total: Long
)
