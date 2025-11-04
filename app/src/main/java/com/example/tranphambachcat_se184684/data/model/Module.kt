package com.example.tranphambachcat_se184684.data.model

import com.google.gson.annotations.SerializedName

/**
 * Course module information
 */
data class Module(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("orderIndex")
    val orderIndex: Int? = 0
)
