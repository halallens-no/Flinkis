package com.halallens.flinkis.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.halallens.flinkis.data.local.entity.ChildProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChildProfileDao {

    @Query("SELECT * FROM child_profiles ORDER BY createdAt ASC")
    fun getAllChildren(): Flow<List<ChildProfileEntity>>

    @Query("SELECT * FROM child_profiles WHERE isActive = 1 LIMIT 1")
    fun getActiveChild(): Flow<ChildProfileEntity?>

    @Query("SELECT * FROM child_profiles WHERE id = :id")
    suspend fun getChildById(id: Long): ChildProfileEntity?

    @Query("SELECT COUNT(*) FROM child_profiles")
    suspend fun getChildCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(child: ChildProfileEntity): Long

    @Update
    suspend fun update(child: ChildProfileEntity)

    @Delete
    suspend fun delete(child: ChildProfileEntity)

    @Query("UPDATE child_profiles SET isActive = 0")
    suspend fun deactivateAll()

    @Query("UPDATE child_profiles SET isActive = 1 WHERE id = :childId")
    suspend fun setActive(childId: Long)

    @Query("UPDATE child_profiles SET themeType = :themeType WHERE id = :childId")
    suspend fun updateTheme(childId: Long, themeType: String)

    @Query("SELECT * FROM child_profiles WHERE isActive = 1 LIMIT 1")
    suspend fun getActiveChildOnce(): ChildProfileEntity?
}
