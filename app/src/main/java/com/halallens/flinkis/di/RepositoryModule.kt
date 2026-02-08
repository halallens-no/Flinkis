package com.halallens.flinkis.di

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Repository Module - Repository bindings
 *
 * Repositories use constructor injection with @Inject @Singleton,
 * so no explicit bindings are needed here.
 * This module exists as a placeholder for future abstract bindings.
 */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule
