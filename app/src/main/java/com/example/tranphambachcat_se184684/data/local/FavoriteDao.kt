package com.example.tranphambachcat_se184684.data.local

import androidx.room.*
import com.example.tranphambachcat_se184684.data.model.Favorite
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteDao {

    @Query("SELECT * FROM favorites ORDER BY timestamp DESC")
    fun getAllFavorites(): Flow<List<Favorite>>

    @Query("SELECT * FROM favorites WHERE courseId = :courseId")
    suspend fun getFavoriteById(courseId: Long): Favorite?

    @Query("SELECT * FROM favorites WHERE courseId = :courseId")
    fun getFavoriteByIdFlow(courseId: Long): Flow<Favorite?>

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE courseId = :courseId)")
    suspend fun isFavorite(courseId: Long): Boolean

    @Query("SELECT EXISTS(SELECT 1 FROM favorites WHERE courseId = :courseId)")
    fun isFavoriteFlow(courseId: Long): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(favorite: Favorite)

    @Delete
    suspend fun deleteFavorite(favorite: Favorite)

    @Query("DELETE FROM favorites WHERE courseId = :courseId")
    suspend fun deleteFavoriteById(courseId: Long)

    @Query("DELETE FROM favorites")
    suspend fun deleteAllFavorites()
}
