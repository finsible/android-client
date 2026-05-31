package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
import com.itsjeel01.finsiblefrontend.ui.component.templates.core.FinsibleSize
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.variant.FinsibleButtonVariant
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FinsibleButtonScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `filled default light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Save", variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filled default dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Save", variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filled loading light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Loading", loading = true, variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filled loading dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Loading", loading = true, variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filled disabled light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Disabled", enabled = false, variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filled disabled dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Disabled", enabled = false, variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `outlined default light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Outlined", variant = FinsibleButtonVariant.Outlined)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `outlined default dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Outlined", variant = FinsibleButtonVariant.Outlined)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filledTonal default light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Tonal", variant = FinsibleButtonVariant.FilledTonal)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filledTonal default dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Tonal", variant = FinsibleButtonVariant.FilledTonal)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `text variant light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Text", variant = FinsibleButtonVariant.Text)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `text variant dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Text", variant = FinsibleButtonVariant.Text)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `link variant light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Link", variant = FinsibleButtonVariant.Link)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `link variant dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Link", variant = FinsibleButtonVariant.Link)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `full width light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).widthIn(max = 360.dp).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Full Width Button", fullWidth = true, variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `full width dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).widthIn(max = 360.dp).padding(16.dp)) {
                    FinsibleButton(onClick = {}, text = "Full Width Button", fullWidth = true, variant = FinsibleButtonVariant.Filled)
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
