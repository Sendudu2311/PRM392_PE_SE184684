package com.example.tranphambachcat_se184684.data.local

import androidx.room.*
import com.example.tranphambachcat_se184684.data.model.ModuleEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ModuleDao {

    @Query("SELECT * FROM modules WHERE courseId = :courseId ORDER BY orderIndex ASC")
    fun getModulesByCourseId(courseId: Long): Flow<List<ModuleEntity>>

    @Query("SELECT * FROM modules WHERE courseId = :courseId ORDER BY orderIndex ASC")
    suspend fun getModulesByCourseIdSync(courseId: Long): List<ModuleEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertModules(modules: List<ModuleEntity>)

    @Query("DELETE FROM modules WHERE courseId = :courseId")
    suspend fun deleteModulesByCourseId(courseId: Long)

    @Query("DELETE FROM modules")
    suspend fun deleteAllModules()
}
