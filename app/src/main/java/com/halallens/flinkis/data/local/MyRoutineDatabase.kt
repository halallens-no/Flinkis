package com.halallens.flinkis.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.halallens.flinkis.data.local.converter.Converters
import com.halallens.flinkis.data.local.dao.ActivityLogDao
import com.halallens.flinkis.data.local.dao.ChildProfileDao
import com.halallens.flinkis.data.local.dao.RewardMilestoneDao
import com.halallens.flinkis.data.local.dao.RoutineDao
import com.halallens.flinkis.data.local.dao.RoutineTemplateDao
import com.halallens.flinkis.data.local.entity.ActivityLogEntity
import com.halallens.flinkis.data.local.entity.ChildProfileEntity
import com.halallens.flinkis.data.local.entity.RewardMilestoneEntity
import com.halallens.flinkis.data.local.entity.RoutineEntity
import com.halallens.flinkis.data.local.entity.RoutineTemplateEntity
import com.halallens.flinkis.data.local.entity.TemplateRoutineEntity

@Database(
    entities = [
        ChildProfileEntity::class,
        RoutineEntity::class,
        ActivityLogEntity::class,
        RewardMilestoneEntity::class,
        RoutineTemplateEntity::class,
        TemplateRoutineEntity::class
    ],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class MyRoutineDatabase : RoomDatabase() {
    abstract fun childProfileDao(): ChildProfileDao
    abstract fun routineDao(): RoutineDao
    abstract fun activityLogDao(): ActivityLogDao
    abstract fun rewardMilestoneDao(): RewardMilestoneDao
    abstract fun routineTemplateDao(): RoutineTemplateDao
}
