@file:JvmName("NavigationRootSharedUtils")

package com.itsjeel01.finsiblefrontend.ui.navigation


private fun <T> popLast(backStack: MutableList<T>) {
    if (backStack.size > 1) {
        backStack.removeAt(backStack.lastIndex)
    }
}
