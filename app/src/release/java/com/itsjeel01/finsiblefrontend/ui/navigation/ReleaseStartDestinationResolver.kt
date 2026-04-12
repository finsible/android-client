package com.itsjeel01.finsiblefrontend.ui.navigation

import javax.inject.Inject

class ReleaseStartDestinationResolver @Inject constructor() : StartDestinationResolver {
    override fun resolveStartDestination(hasShownTestScreen: Boolean): Route = Route.Launch
}

