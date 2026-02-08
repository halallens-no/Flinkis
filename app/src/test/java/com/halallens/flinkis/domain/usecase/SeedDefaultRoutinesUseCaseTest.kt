package com.halallens.flinkis.domain.usecase

import com.halallens.flinkis.data.repository.RoutineRepository
import com.halallens.flinkis.util.Constants
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any

class SeedDefaultRoutinesUseCaseTest {

    private lateinit var routineRepository: RoutineRepository
    private lateinit var useCase: SeedDefaultRoutinesUseCase

    @Before
    fun setup() {
        routineRepository = mock(RoutineRepository::class.java)
        useCase = SeedDefaultRoutinesUseCase(routineRepository)
    }

    @Test
    fun `invoke seeds routines when count is zero`() = runTest {
        `when`(routineRepository.getRoutineCount(1L)).thenReturn(0)

        useCase(1L)

        val expected = Constants.getDefaultRoutines(1L)
        verify(routineRepository).createRoutines(expected)
    }

    @Test
    fun `invoke does not seed when routines already exist`() = runTest {
        `when`(routineRepository.getRoutineCount(1L)).thenReturn(5)

        useCase(1L)

        verify(routineRepository, never()).createRoutines(any())
    }
}
