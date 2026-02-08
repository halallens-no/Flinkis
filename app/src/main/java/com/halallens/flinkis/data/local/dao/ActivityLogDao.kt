package com.halallens.flinkis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.halallens.flinkis.data.local.entity.ActivityLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ActivityLogDao {

    @Query("SELECT * FROM activity_logs WHERE childId = :childId AND date = :date")
    fun getLogsForDate(childId: Long, date: String): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs WHERE childId = :childId AND date BETWEEN :startDate AND :endDate")
    fun getLogsForDateRange(childId: Long, startDate: String, endDate: String): Flow<List<ActivityLogEntity>>

    @Query("SELECT * FROM activity_logs WHERE routineId = :routineId AND date = :date LIMIT 1")
    suspend fun getLog(routineId: Long, date: String): ActivityLogEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(log: ActivityLogEntity): Long

    @Update
    suspend fun update(log: ActivityLogEntity)

    @Query("SELECT SUM(pointsEarned) FROM activity_logs WHERE childId = :childId AND completed = 1")
    fun getTotalPoints(childId: Long): Flow<Int?>

    @Query("SELECT DISTINCT date FROM activity_logs WHERE childId = :childId AND completed = 1 ORDER BY date DESC")
    suspend fun getCompletedDates(childId: Long): List<String>

    @Query("SELECT COUNT(*) FROM activity_logs WHERE childId = :childId AND date = :date AND completed = 1")
    suspend fun getCompletedCountForDate(childId: Long, date: String): Int
}
