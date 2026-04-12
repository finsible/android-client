package com.itsjeel01.finsiblefrontend.ui.navigation

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DebugNavigationModule {
	@Binds
	@Singleton
	abstract fun bindStartDestinationResolver(
		impl: DebugStartDestinationResolver
	): StartDestinationResolver
}


