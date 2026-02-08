package com.halallens.flinkis.data.repository

import com.halallens.flinkis.data.local.dao.ChildProfileDao
import com.halallens.flinkis.data.local.entity.ChildProfileEntity
import com.halallens.flinkis.domain.model.ChildProfile
import com.halallens.flinkis.domain.model.ThemeType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChildRepository @Inject constructor(
    private val childProfileDao: ChildProfileDao
) {
    fun getAllChildren(): Flow<List<ChildProfile>> =
        childProfileDao.getAllChildren().map { entities ->
            entities.map { it.toDomain() }
        }

    fun getActiveChild(): Flow<ChildProfile?> =
        childProfileDao.getActiveChild().map { it?.toDomain() }

    suspend fun createChild(child: ChildProfile): Long {
        val isFirst = childProfileDao.getChildCount() == 0
        val entity = child.copy(isActive = isFirst).toEntity()
        return childProfileDao.insert(entity)
    }

    suspend fun updateChild(child: ChildProfile) {
        childProfileDao.update(child.toEntity())
    }

    suspend fun deleteChild(child: ChildProfile) {
        childProfileDao.delete(child.toEntity())
    }

    suspend fun switchActiveChild(childId: Long) {
        childProfileDao.deactivateAll()
        childProfileDao.setActive(childId)
    }

    suspend fun updateChildTheme(childId: Long, themeType: ThemeType) {
        childProfileDao.updateTheme(childId, themeType.name)
    }

    suspend fun getActiveChildOnce(): ChildProfile? =
        childProfileDao.getActiveChildOnce()?.toDomain()

    private fun ChildProfileEntity.toDomain() = ChildProfile(
        id = id,
        name = name,
        avatarId = avatarId,
        themeType = ThemeType.valueOf(themeType),
        isActive = isActive,
        createdAt = createdAt
    )

    private fun ChildProfile.toEntity() = ChildProfileEntity(
        id = id,
        name = name,
        avatarId = avatarId,
        themeType = themeType.name,
        isActive = isActive,
        createdAt = createdAt
    )
}
