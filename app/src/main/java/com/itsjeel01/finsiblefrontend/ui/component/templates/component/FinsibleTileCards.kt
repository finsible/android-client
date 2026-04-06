package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Density
import com.itsjeel01.finsiblefrontend.R
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.default.FinsibleTileCardDefaults
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardColors
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardData
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.FinsibleTileCardSizes
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTileCardRotationVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleScrubberVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.launch

/** Renders tile cards as a carousel or sequential flip rotator using template tokens.
 * @param cards Immutable list of tile card models to render.
 * @param modifier Modifier applied to the tile cards container.
 * @param size Size token used to resolve dimensions and typography.
 * @param rotationVariant Rotation behavior for carousel or sequential mode.
 * @param currentIndex Optional externally controlled selected index.
 * @param onIndexChange Callback invoked when the selected index changes.
 * @param decorativeIcon Optional decorative icon slot shown in non-sequential mode.
 * @param inverted Whether to use inverted foreground color resolution in defaults.
 * @param carouselCardFraction Width fraction used for each carousel page.
 * @param colors Color tokens used to render tile card surfaces and content.
 * @param sizes Size tokens used to render tile card spacing and typography.
 */
@Composable
fun FinsibleTileCards(
    cards: ImmutableList<FinsibleTileCardData>,
    modifier: Modifier = Modifier,
    size: FinsibleSize = FinsibleSize.Medium,
    rotationVariant: FinsibleTileCardRotationVariant = FinsibleTileCardRotationVariant.Carousel,
    currentIndex: Int? = null,
    onIndexChange: ((Int) -> Unit)? = null,
    decorativeIcon: (@Composable () -> Unit)? = null,
    inverted: Boolean = false,
    carouselCardFraction: Float = 0.92f,
    colors: FinsibleTileCardColors = FinsibleTileCardDefaults.colors(inverted = inverted),
    sizes: FinsibleTileCardSizes = FinsibleTileCardDefaults.sizes(size)
) {
    require(cards.isNotEmpty()) { "cards must not be empty." }
    require(cards.all { it.statistics.size <= allowedStats(size) }) { "Statistics exceed allowed count for size $size." }
    require(carouselCardFraction in 0f .. 1f && carouselCardFraction > 0f) { "carouselCardFraction must be within (0, 1]." }

    val shape = RoundedCornerShape(sizes.cornerRadius)
    val scope = rememberCoroutineScope()
    val rotation = remember { Animatable(0f) }
    val elasticEasing = remember { CubicBezierEasing(0.68f, -0.55f, 0.265f, 1.55f) }
    val isControlled = currentIndex != null

    var internalIndex by remember { mutableIntStateOf((currentIndex ?: 0).coerceIn(0, cards.lastIndex)) }
    val selectedIndex = (currentIndex ?: internalIndex).coerceIn(0, cards.lastIndex)
    var displayedIndex by remember { mutableIntStateOf(selectedIndex) }
    var targetIndex by remember { mutableIntStateOf(selectedIndex) }
    var isFlipping by remember { mutableStateOf(false) }

    fun flipToIndex(next: Int, notifyParent: Boolean) {
        val bounded = next.coerceIn(0, cards.lastIndex)
        if (bounded == displayedIndex || isFlipping) return

        if (isControlled && notifyParent) {
            onIndexChange?.invoke(bounded)
            return
        }

        isFlipping = true
        targetIndex = bounded

        scope.launch {
            rotation.animateTo(
                targetValue = 180f,
                animationSpec = tween(
                    durationMillis = Duration.MS_800.toInt(),
                    easing = elasticEasing
                )
            )
            displayedIndex = bounded
            if (!isControlled) {
                internalIndex = bounded
            }
            rotation.snapTo(0f)
            isFlipping = false

            if (notifyParent) {
                onIndexChange?.invoke(bounded)
            }
        }
    }

    val currentRotation = rotation.value % 360f
    val showFront = currentRotation !in 90f ..< 270f
    val rotateCardLabel = stringResource(R.string.component_tile_cards_next_card)

    val pagerState = if (rotationVariant == FinsibleTileCardRotationVariant.Carousel) {
        rememberPagerState(
            initialPage = selectedIndex,
            pageCount = { cards.size }
        )
    } else null

    LaunchedEffect(currentIndex, cards.size, isFlipping) {
        val bounded = (currentIndex ?: internalIndex).coerceIn(0, cards.lastIndex)

        if (rotationVariant == FinsibleTileCardRotationVariant.Sequential) {
            if (!isFlipping && bounded != displayedIndex) {
                targetIndex = bounded
                flipToIndex(bounded, notifyParent = false)
            } else {
                targetIndex = bounded
                displayedIndex = displayedIndex.coerceIn(0, cards.lastIndex)
            }
        } else {
            if (pagerState != null && !pagerState.isScrollInProgress && bounded != pagerState.currentPage) {
                pagerState.animateScrollToPage(bounded)
            }
        }
    }

    if (rotationVariant == FinsibleTileCardRotationVariant.Carousel && pagerState != null) {
        LaunchedEffect(pagerState) {
            snapshotFlow { pagerState.currentPage }.collect { page ->
                if (displayedIndex != page) {
                    displayedIndex = page
                    if (!isControlled) {
                        internalIndex = page
                    }
                    onIndexChange?.invoke(page)
                }
            }
        }
    }

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.dimes.d12)
    ) {
        if (rotationVariant == FinsibleTileCardRotationVariant.Carousel && pagerState != null) {
            val customPageSize = remember(carouselCardFraction) {
                object : PageSize {
                    override fun Density.calculateMainAxisPageSize(availableSpace: Int, pageSpacing: Int): Int {
                        return (availableSpace * carouselCardFraction).toInt()
                    }
                }
            }
            HorizontalPager(
                state = pagerState,
                pageSize = customPageSize,
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = FinsibleTheme.dimes.d16),
                pageSpacing = FinsibleTheme.dimes.d16
            ) { page ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                ) {
                    TileCardFace(
                        card = cards[page],
                        colors = colors,
                        sizes = sizes,
                        shape = shape,
                        rotationVariant = rotationVariant,
                        decorativeIcon = decorativeIcon,
                        isFront = true,
                        showFace = true
                    )
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .graphicsLayer {
                        cameraDistance = 12f * density
                        rotationY = rotation.value
                        transformOrigin = TransformOrigin.Center
                    }
                    .clip(shape)
                    .clickable(
                        role = Role.Button,
                        onClickLabel = rotateCardLabel
                    ) {
                        flipToIndex((displayedIndex + 1) % cards.size, notifyParent = true)
                    }
            ) {
                TileCardFace(
                    card = cards[targetIndex],
                    colors = colors,
                    sizes = sizes,
                    shape = shape,
                    rotationVariant = rotationVariant,
                    decorativeIcon = decorativeIcon,
                    isFront = false,
                    showFace = !showFront
                )

                TileCardFace(
                    card = cards[displayedIndex],
                    colors = colors,
                    sizes = sizes,
                    shape = shape,
                    rotationVariant = rotationVariant,
                    decorativeIcon = decorativeIcon,
                    isFront = true,
                    showFace = showFront
                )
            }
        }

        if (rotationVariant == FinsibleTileCardRotationVariant.Carousel && pagerState != null) {
            FinsibleScrubber(
                totalCount = cards.size,
                currentIndex = pagerState.currentPage,
                onIndexChange = {
                    scope.launch { pagerState.animateScrollToPage(it) }
                },
                modifier = Modifier.fillMaxWidth(),
                variant = FinsibleScrubberVariant.Separate
            )
        }
    }
}


