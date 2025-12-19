package com.itsjeel01.finsiblefrontend.ui.util

import androidx.compose.animation.ContentTransform
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.ui.unit.IntOffset
import com.itsjeel01.finsiblefrontend.ui.constants.Duration

/** Calculates a horizontal slide transition based on the index order of routes. */
fun calculateIndexedTransition(
    routeOrder: List<Any>,
    initialKey: Any?,
    targetKey: Any?,
    durationMillis: Int = Duration.MS_300.toInt()
): ContentTransform {
    val initialIndex = routeOrder.indexOfFirst { 
        it == initialKey || (initialKey != null && it.toString() == initialKey.toString())
    }
    val targetIndex = routeOrder.indexOfFirst { 
        it == targetKey || (targetKey != null && it.toString() == targetKey.toString())
    }

    val slideSpec = tween<IntOffset>(durationMillis = durationMillis, easing = FastOutSlowInEasing)

    return if (initialIndex != -1 && targetIndex != -1 && targetIndex > initialIndex) {
        slideInHorizontally(slideSpec) { width -> width } togetherWith
            slideOutHorizontally(slideSpec) { width -> -width }
    } else {
        slideInHorizontally(slideSpec) { width -> -width } togetherWith
            slideOutHorizontally(slideSpec) { width -> width }
    }
}
