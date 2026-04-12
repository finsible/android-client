package com.itsjeel01.finsiblefrontend.ui.navigation

import com.itsjeel01.finsiblefrontend.common.TestPreferenceManager
import javax.inject.Inject

class DebugStartDestinationResolver @Inject constructor(
	private val testPreferenceManager: TestPreferenceManager
) : StartDestinationResolver {
	override fun resolveStartDestination(hasShownTestScreen: Boolean): Route {
		return if (!testPreferenceManager.shouldSkipDebugScreen() && !hasShownTestScreen) {
			Route.Test
		} else {
			Route.Launch
		}
	}
}

