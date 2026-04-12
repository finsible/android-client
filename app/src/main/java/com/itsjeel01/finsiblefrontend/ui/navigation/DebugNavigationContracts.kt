package com.itsjeel01.finsiblefrontend.ui.navigation

interface StartDestinationResolver {
	fun resolveStartDestination(hasShownTestScreen: Boolean): Route
}


