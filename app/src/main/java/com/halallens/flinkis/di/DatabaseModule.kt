package com.halallens.flinkis.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.halallens.flinkis.data.local.MyRoutineDatabase
import com.halallens.flinkis.data.local.dao.ActivityLogDao
import com.halallens.flinkis.data.local.dao.ChildProfileDao
import com.halallens.flinkis.data.local.dao.RewardMilestoneDao
import com.halallens.flinkis.data.local.dao.RoutineDao
import com.halallens.flinkis.data.local.dao.RoutineTemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Database Module - Provides Room database and DAOs
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    companion object {

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `routine_templates` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `name` TEXT NOT NULL,
                        `description` TEXT NOT NULL,
                        `category` TEXT NOT NULL,
                        `iconName` TEXT NOT NULL,
                        `createdAt` INTEGER NOT NULL
                    )"""
                )
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS `template_routines` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `templateId` INTEGER NOT NULL,
                        `name` TEXT NOT NULL,
                        `timeSlot` TEXT NOT NULL,
                        `points` INTEGER NOT NULL DEFAULT 1,
                        `iconName` TEXT NOT NULL,
                        `daysOfWeek` TEXT NOT NULL DEFAULT '1,2,3,4,5,6,7',
                        `sortOrder` INTEGER NOT NULL DEFAULT 0,
                        FOREIGN KEY(`templateId`) REFERENCES `routine_templates`(`id`) ON DELETE CASCADE
                    )"""
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS `index_template_routines_templateId` ON `template_routines` (`templateId`)"
                )
            }
        }

        @JvmStatic
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): MyRoutineDatabase {
            return Room.databaseBuilder(
                context,
                MyRoutineDatabase::class.java,
                "myroutine_database"
            )
                .addMigrations(MIGRATION_1_2)
                .build()
        }

        @JvmStatic
        @Provides
        fun provideChildProfileDao(database: MyRoutineDatabase): ChildProfileDao =
            database.childProfileDao()

        @JvmStatic
        @Provides
        fun provideRoutineDao(database: MyRoutineDatabase): RoutineDao =
            database.routineDao()

        @JvmStatic
        @Provides
        fun provideActivityLogDao(database: MyRoutineDatabase): ActivityLogDao =
            database.activityLogDao()

        @JvmStatic
        @Provides
        fun provideRewardMilestoneDao(database: MyRoutineDatabase): RewardMilestoneDao =
            database.rewardMilestoneDao()

        @JvmStatic
        @Provides
        fun provideRoutineTemplateDao(database: MyRoutineDatabase): RoutineTemplateDao =
            database.routineTemplateDao()
    }
}
