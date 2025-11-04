package com.example.tranphambachcat_se184684.data.model

import com.google.gson.annotations.SerializedName

/**
 * Detailed course information from API
 * Used for detail screen
 */
data class CourseDetail(
    @SerializedName("id")
    val id: Long,

    @SerializedName("title")
    val title: String,

    @SerializedName("description")
    val description: String? = null,

    @SerializedName("level")
    val level: String? = null,

    @SerializedName("status")
    val status: String? = "PUBLIC",

    @SerializedName("author")
    val author: Author? = null,

    @SerializedName("modules")
    val modules: List<Module>? = null,

    @SerializedName("price")
    val price: Double? = null,

    @SerializedName("currency")
    val currency: String? = "VND",

    @SerializedName("authorName")
    val authorName: String? = null,

    @SerializedName("thumbnailUrl")
    val thumbnailUrl: String? = null,

    @SerializedName("enrollmentCount")
    val enrollmentCount: Int? = 0,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("publishedDate")
    val publishedDate: String? = null,

    // Local field for favorite status
    var isFavorite: Boolean = false
) {
    /**
     * Convert CourseDetail to Course entity for database storage
     */
    fun toCourse(isFavorite: Boolean = this.isFavorite): Course {
        return Course(
            id = id,
            title = title,
            description = description,
            level = level,
            status = status,
            price = price,
            currency = currency,
            authorName = authorName ?: author?.fullName,
            thumbnailUrl = thumbnailUrl,
            enrollmentCount = enrollmentCount,
            moduleCount = modules?.size,
            createdAt = createdAt,
            updatedAt = updatedAt,
            publishedDate = publishedDate,
            isFavorite = isFavorite
        )
    }
}
