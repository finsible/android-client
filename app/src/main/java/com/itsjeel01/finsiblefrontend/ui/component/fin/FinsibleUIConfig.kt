package com.itsjeel01.finsiblefrontend.ui.component.fin

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme

// --- ComponentSize Enum - Defines sizes for components ---

enum class ComponentSize {
    Small, Medium, Large;

    val buttonHeight: Dp
        @Composable
        get() = when (this) {
            Small -> FinsibleTheme.dimes.d32
            Medium -> FinsibleTheme.dimes.d48
            Large -> FinsibleTheme.dimes.d56
        }

    val cornerRadius: Dp
        @Composable
        get() = when (this) {
            Small -> FinsibleTheme.dimes.d8
            Medium -> FinsibleTheme.dimes.d12
            Large -> FinsibleTheme.dimes.d12
        }

    val iconSize: Dp
        @Composable
        get() = when (this) {
            Small -> FinsibleTheme.dimes.d16
            Medium -> FinsibleTheme.dimes.d20
            Large -> FinsibleTheme.dimes.d24
        }

    val iconButtonPadding: Dp
        @Composable
        get() = when (this) {
            Small -> FinsibleTheme.dimes.d4
            Medium -> FinsibleTheme.dimes.d8
            Large -> FinsibleTheme.dimes.d8
        }

    val horizontalPadding: Dp
        @Composable
        get() = when (this) {
            Small -> FinsibleTheme.dimes.d12
            Medium -> FinsibleTheme.dimes.d16
            Large -> FinsibleTheme.dimes.d20
        }

    val loadingIndicatorSize: Dp
        @Composable
        get() = when (this) {
            Small -> FinsibleTheme.dimes.d24
            Medium -> FinsibleTheme.dimes.d48
            Large -> FinsibleTheme.dimes.d72
        }

    @Composable
    fun typography(): TextStyle {
        return when (this) {
            Small -> FinsibleTheme.typography.uiSmall
            Medium -> FinsibleTheme.typography.uiMedium
            Large -> FinsibleTheme.typography.uiLarge
        }
    }
}

// --- ComponentType Enum - Defines visual styles for components ---

enum class ComponentType {
    Brand, Primary, Secondary, Tertiary;

    @Composable
    fun containerColor(): Color = when (this) {
        Brand -> FinsibleTheme.colors.brandAccent
        Primary -> FinsibleTheme.colors.primaryContent
        Secondary -> Color.Transparent
        Tertiary -> Color.Transparent
    }

    @Composable
    fun contentColor(): Color = when (this) {
        Brand -> FinsibleTheme.colors.primaryBackground
        Primary -> FinsibleTheme.colors.primaryBackground
        Secondary -> FinsibleTheme.colors.primaryContent
        Tertiary -> FinsibleTheme.colors.primaryContent60
    }

    @Composable
    fun borderColor(): Color? = when (this) {
        Secondary -> FinsibleTheme.colors.border
        else -> null
    }
}

// --- IconPosition Enum - Defines icon placement relative to text in FinsibleButton ---

enum class IconPosition { Leading, Trailing, BeforeLabel, AfterLabel }

// --- IconButtonShape Enum - Defines shapes for FinsibleIconButton ---

enum class IconButtonShape {
    Circle, Square, Rounded;

    @Composable
    fun shape(size: ComponentSize): Shape = when (this) {
        Circle -> CircleShape
        Square -> RectangleShape
        Rounded -> RoundedCornerShape(size.cornerRadius)
    }
}

// --- LoadingSpeed Enum - Defines animation speeds for loading indicators ---

enum class LoadingSpeed(val durationMs: Int) {
    FAST(1200),
    NORMAL(2000),
    SLOW(2400);
}
