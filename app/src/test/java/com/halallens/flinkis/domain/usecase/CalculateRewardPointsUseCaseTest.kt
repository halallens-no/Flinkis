package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.ActivityLogRepository
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CalculateRewardPointsUseCaseTest {

    private lateinit var activityLogRepository: ActivityLogRepository
    private lateinit var useCase: CalculateRewardPointsUseCase

    @Before
    fun setup() {
        activityLogRepository = mock(ActivityLogRepository::class.java)
        useCase = CalculateRewardPointsUseCase(activityLogRepository)
    }

    @Test
    fun `getTotalPoints returns flow from repository`() = runTest {
        `when`(activityLogRepository.getTotalPoints(1L)).thenReturn(flowOf(42))

        val result = useCase.getTotalPoints(1L).first()
        assertEquals(42, result)
    }

    @Test
    fun `calculateStreak returns 0 when no completed dates`() = runTest {
        `when`(activityLogRepository.getCompletedDates(1L)).thenReturn(emptyList())

        val result = useCase.calculateStreak(1L)
        assertEquals(0, result)
    }

    @Test
    fun `calculateStreak returns 1 when only today completed`() = runTest {
        val today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
        `when`(activityLogRepository.getCompletedDates(1L)).thenReturn(listOf(today))

        val result = useCase.calculateStreak(1L)
        assertEquals(1, result)
    }

    @Test
    fun `calculateStreak returns consecutive days count`() = runTest {
        val today = LocalDate.now()
        val dates = (0L..4L).map {
            today.minusDays(it).format(DateTimeFormatter.ISO_LOCAL_DATE)
        }
        `when`(activityLogRepository.getCompletedDates(1L)).thenReturn(dates)

        val result = useCase.calculateStreak(1L)
        assertEquals(5, result)
    }

    @Test
    fun `calculateStreak breaks on gap`() = runTest {
        val today = LocalDate.now()
        val dates = listOf(
            today.format(DateTimeFormatter.ISO_LOCAL_DATE),
            today.minusDays(1).format(DateTimeFormatter.ISO_LOCAL_DATE),
            today.minusDays(3).format(DateTimeFormatter.ISO_LOCAL_DATE) // gap at day 2
        )
        `when`(activityLogRepository.getCompletedDates(1L)).thenReturn(dates)

        val result = useCase.calculateStreak(1L)
        assertEquals(2, result)
    }
}
