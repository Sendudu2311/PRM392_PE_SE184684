package com.example.tranphambachcat_se184684.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Favorite courses entity
 * Stores course IDs that user has marked as favorite
 */
@Entity(tableName = "favorites")
data class Favorite(
    @PrimaryKey
    val courseId: Long,
    val timestamp: Long = System.currentTimeMillis()
)
