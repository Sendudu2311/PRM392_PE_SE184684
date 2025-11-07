package com.example.tranphambachcat_se184684.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Module entity for storing course modules
 */
@Entity(tableName = "modules")
data class ModuleEntity(
    @PrimaryKey
    val id: Long,

    val courseId: Long,  // Foreign key to Course

    val title: String,

    val description: String? = null,

    val orderIndex: Int? = 0
)

/**
 * Extension to convert Module API model to ModuleEntity
 */
fun Module.toEntity(courseId: Long): ModuleEntity {
    return ModuleEntity(
        id = this.id,
        courseId = courseId,
        title = this.title,
        description = this.description,
        orderIndex = this.orderIndex
    )
}