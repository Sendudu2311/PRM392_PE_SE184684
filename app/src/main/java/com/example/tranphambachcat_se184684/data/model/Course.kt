package com.example.tranphambachcat_se184684.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

/**
 * Course entity for Room database and API response
 * Maps to SkillVerse Course API
 */
@Entity(tableName = "courses")
data class Course(
    @PrimaryKey
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

    @SerializedName("moduleCount")
    val moduleCount: Int? = 0,

    @SerializedName("createdAt")
    val createdAt: String? = null,

    @SerializedName("updatedAt")
    val updatedAt: String? = null,

    @SerializedName("publishedDate")
    val publishedDate: String? = null,

    // Local field for favorite functionality (not from API)
    val isFavorite: Boolean = false,

    // Timestamp for cache management
    val cacheTimestamp: Long = System.currentTimeMillis()
)
