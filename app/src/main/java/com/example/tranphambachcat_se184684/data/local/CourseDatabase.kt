package com.example.tranphambachcat_se184684.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tranphambachcat_se184684.data.model.Course
import com.example.tranphambachcat_se184684.data.model.Favorite
import com.example.tranphambachcat_se184684.data.model.ModuleEntity
import com.example.tranphambachcat_se184684.data.local.ModuleDao

@Database(
    entities = [Course::class, Favorite::class, ModuleEntity::class],
    version = 2,  // Increment version
    exportSchema = false
)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun favoriteDao(): FavoriteDao
    abstract fun moduleDao(): ModuleDao

    companion object {
        const val DATABASE_NAME = "course_database"
    }
}
