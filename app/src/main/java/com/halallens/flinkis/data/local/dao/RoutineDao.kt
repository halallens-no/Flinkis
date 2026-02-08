package com.halallens.flinkis.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.halallens.flinkis.data.local.entity.RoutineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RoutineDao {

    @Query("SELECT * FROM routines WHERE childId = :childId AND isActive = 1 ORDER BY timeSlot, sortOrder")
    fun getRoutinesForChild(childId: Long): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE childId = :childId AND isActive = 1 AND daysOfWeek LIKE '%' || :dayOfWeek || '%' ORDER BY timeSlot, sortOrder")
    fun getRoutinesForDay(childId: Long, dayOfWeek: Int): Flow<List<RoutineEntity>>

    @Query("SELECT * FROM routines WHERE id = :id")
    suspend fun getRoutineById(id: Long): RoutineEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(routine: RoutineEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(routines: List<RoutineEntity>)

    @Update
    suspend fun update(routine: RoutineEntity)

    @Delete
    suspend fun delete(routine: RoutineEntity)

    @Query("SELECT COUNT(*) FROM routines WHERE childId = :childId AND isActive = 1")
    suspend fun getRoutineCount(childId: Long): Int

    @Query("SELECT * FROM routines WHERE childId = :childId AND isActive = 1")
    suspend fun getRoutineListForChild(childId: Long): List<RoutineEntity>

    @Query("DELETE FROM routines WHERE childId = :childId")
    suspend fun deleteAllRoutinesForChild(childId: Long)
}
