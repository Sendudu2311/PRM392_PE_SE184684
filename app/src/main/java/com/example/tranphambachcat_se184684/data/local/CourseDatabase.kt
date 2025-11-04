package com.example.tranphambachcat_se184684.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tranphambachcat_se184684.data.model.Course
import com.example.tranphambachcat_se184684.data.model.Favorite

@Database(
    entities = [Course::class, Favorite::class],
    version = 1,
    exportSchema = false
)
abstract class CourseDatabase : RoomDatabase() {
    abstract fun courseDao(): CourseDao
    abstract fun favoriteDao(): FavoriteDao

    companion object {
        const val DATABASE_NAME = "course_database"
    }
}
