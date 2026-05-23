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
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleScrubberVariant
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleTileCardRotationVariant
import com.itsjeel01.finsiblefrontend.ui.constants.Duration
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleDurations
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
        targetIndex = bounded // The back face gets the new data immediately

        scope.launch {
            // 1. Single, continuous animation from 0 to 180
            rotation.animateTo(
                targetValue = 180f,
                animationSpec = tween(
                    durationMillis = FinsibleDurations.values.slowMs,
                    easing = elasticEasing
                )
            )

            // 2. Swap the front face to the new data only AFTER the animation finishes
            displayedIndex = bounded
            if (!isControlled) {
                internalIndex = bounded
            }

            // 3. Snap back to 0 seamlessly
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
        verticalArrangement = Arrangement.spacedBy(FinsibleTheme.spacing.stackMd)
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
                contentPadding = PaddingValues(horizontal = FinsibleTheme.spacing.insetLg),
                pageSpacing = FinsibleTheme.spacing.insetLg
            ) { page ->
                val card = cards[page]
                val cardColors = resolveCardColors(card = card, colors = colors, inverted = inverted)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape)
                ) {
                    TileCardFace(
                        card = card,
                        colors = cardColors,
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
            // MATCHING FLIPPABLE CARD: Always keep the back card calculated, drawn, and ready
            val frontIndex = displayedIndex
            val backIndex = if (isControlled) targetIndex else (displayedIndex + 1) % cards.size

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
                        // Request flip to the eagerly calculated next card
                        val nextToFlip = if (isControlled) (displayedIndex + 1) % cards.size else backIndex
                        flipToIndex(nextToFlip, notifyParent = true)
                    }
            ) {
                // BACK CARD (Drawn pre-rotated 180deg and hidden/ready)
                val backCard = cards[backIndex]
                val backCardColors = resolveCardColors(card = backCard, colors = colors, inverted = inverted)
                TileCardFace(
                    card = backCard,
                    colors = backCardColors,
                    sizes = sizes,
                    shape = shape,
                    rotationVariant = rotationVariant,
                    decorativeIcon = decorativeIcon,
                    isFront = false,
                    showFace = !showFront
                )

                // FRONT CARD
                val frontCard = cards[frontIndex]
                val frontCardColors = resolveCardColors(card = frontCard, colors = colors, inverted = inverted)
                TileCardFace(
                    card = frontCard,
                    colors = frontCardColors,
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

@Composable
private fun resolveCardColors(
    card: FinsibleTileCardData,
    colors: FinsibleTileCardColors,
    inverted: Boolean
): FinsibleTileCardColors {
    val cardInverted = card.inverted ?: inverted
    return if (card.inverted == null || cardInverted == inverted) {
        colors
    } else {
        FinsibleTileCardDefaults.colors(inverted = cardInverted)
    }
}


