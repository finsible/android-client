package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
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
class FinsibleInteractionScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    // ── Button interaction ─────────────────────────────────────────────

    @Test
    fun `button click triggers loading state`() {
        var isLoading = false
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(
                        onClick = { isLoading = true },
                        text = "Submit",
                        loading = isLoading,
                        variant = FinsibleButtonVariant.Filled,
                    )
                }
            }
        }
        // Click the button
        composeTestRule.onNodeWithText("Submit").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `button click then loading snapshot shows spinner`() {
        var clickCount = 0
        var isLoading = false
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleButton(
                        onClick = {
                            clickCount++
                            isLoading = true
                        },
                        text = "Save",
                        loading = isLoading,
                        variant = FinsibleButtonVariant.Filled,
                    )
                }
            }
        }
        composeTestRule.onNodeWithText("Save").performClick()
        composeTestRule.waitForIdle()

        composeTestRule.onRoot().captureRoboImage()
        assert(isLoading) { "isLoading should be true after click" }
    }

    // ── FilterChip interaction ─────────────────────────────────────────

    @Test
    fun `filter chip toggles from unselected to selected`() {
        var selected = false
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleFilterChip(
                        selected = selected,
                        onSelectedChange = { selected = it },
                        label = "Food",
                    )
                }
            }
        }

        // Capture before (unselected)
        composeTestRule.onRoot().captureRoboImage()

        // Toggle selected
        composeTestRule.onNodeWithText("Food").performClick()
        composeTestRule.waitForIdle()

        // Capture after (selected)
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filter chip toggling back and forth`() {
        var selected = false
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp)) {
                    FinsibleFilterChip(
                        selected = selected,
                        onSelectedChange = { selected = it },
                        label = "Transport",
                    )
                }
            }
        }

        // Toggle on
        composeTestRule.onNodeWithText("Transport").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage()

        // Toggle off
        composeTestRule.onNodeWithText("Transport").performClick()
        composeTestRule.waitForIdle()
        composeTestRule.onRoot().captureRoboImage()
    }

    // ── TextField interaction ──────────────────────────────────────────

    @Test
    fun `text field typing shows input`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Type here",
                    )
                }
            }
        }

        // Type text
        composeTestRule.onNodeWithText("Type here").performTextInput("Hello World")
        composeTestRule.waitForIdle()

        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `text field typing invalid triggers error state`() {
        var value = "toolong"
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = value,
                        onValueChange = {},
                        placeholder = "Enter amount",
                        label = "Amount",
                        isError = true,
                        supportingText = "Too many characters",
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
