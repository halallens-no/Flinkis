package com.halallens.flinkis.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.halallens.flinkis.data.local.entity.RewardMilestoneEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RewardMilestoneDao {

    @Query("SELECT * FROM reward_milestones WHERE childId = :childId ORDER BY requiredPoints ASC")
    fun getMilestonesForChild(childId: Long): Flow<List<RewardMilestoneEntity>>

    @Query("SELECT * FROM reward_milestones WHERE childId = :childId AND isEarned = 1")
    fun getEarnedMilestones(childId: Long): Flow<List<RewardMilestoneEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(milestones: List<RewardMilestoneEntity>)

    @Update
    suspend fun update(milestone: RewardMilestoneEntity)

    @Query("SELECT COUNT(*) FROM reward_milestones WHERE childId = :childId")
    suspend fun getMilestoneCount(childId: Long): Int
}
