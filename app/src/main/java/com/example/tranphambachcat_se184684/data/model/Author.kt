package com.example.tranphambachcat_se184684.data.model

import com.google.gson.annotations.SerializedName

/**
 * Author information from API response
 */
data class Author(
    @SerializedName("id")
    val id: Long,

    @SerializedName("email")
    val email: String? = null,

    @SerializedName("firstName")
    val firstName: String? = null,

    @SerializedName("lastName")
    val lastName: String? = null,

    @SerializedName("fullName")
    val fullName: String? = null,

    @SerializedName("roles")
    val roles: List<String>? = null,

    @SerializedName("authProvider")
    val authProvider: String? = null,

    @SerializedName("googleLinked")
    val googleLinked: Boolean? = false
)
