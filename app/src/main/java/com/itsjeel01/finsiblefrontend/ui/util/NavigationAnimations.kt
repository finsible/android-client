package com.itsjeel01.finsiblefrontend.ui.util

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset

/**
 * Calculates a horizontal slide transition based on the index order of routes.
 *
 * @param routeOrder List of routes in the desired order
 * @param initialKey The initial route (current state)
 * @param targetKey The target route (destination state)
 * @param durationMillis Duration of the transition animation in milliseconds (default: 300ms)
 * @return ContentTransform with horizontal slide animation based on route order
 *
 * If either route is not found in routeOrder, defaults to left-to-right slide animation.
 * String comparison is used as a fallback to match routes that may have been serialized/deserialized.
 */
fun calculateIndexedTransition(
    routeOrder: List<Any>,
    initialKey: Any?,
    targetKey: Any?,
    durationMillis: Int = 300
): ContentTransform {
    fun findRouteIndex(key: Any?): Int {
        return routeOrder.indexOfFirst { route ->
            route == key || (key != null && route.toString() == key.toString())
        }
    }

    val initialIndex = findRouteIndex(initialKey)
    val targetIndex = findRouteIndex(targetKey)

    val slideSpec = tween<IntOffset>(durationMillis = durationMillis, easing = FastOutSlowInEasing)

    return if (initialIndex != -1 && targetIndex != -1 && targetIndex > initialIndex) {
        slideInHorizontally(slideSpec) { width -> width } togetherWith
            slideOutHorizontally(slideSpec) { width -> -width }
    } else {
        slideInHorizontally(slideSpec) { width -> -width } togetherWith
            slideOutHorizontally(slideSpec) { width -> width }
    }
}
