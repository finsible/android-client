package com.itsjeel01.finsiblefrontend.ui.component.templates.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.unit.dp
import com.github.takahirom.roborazzi.captureRoboImage
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
class FinsibleTextFieldScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `default empty light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(value = "", onValueChange = {}, placeholder = "Enter value")
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `default empty dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(value = "", onValueChange = {}, placeholder = "Enter value")
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filled with text light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(value = "Hello World", onValueChange = {}, placeholder = "Placeholder")
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `filled with text dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(value = "Hello World", onValueChange = {}, placeholder = "Placeholder")
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `with label and supporting text light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Enter email",
                        label = "Email",
                        supportingText = "We'll never share your email"
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `with label and supporting text dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = "",
                        onValueChange = {},
                        placeholder = "Enter email",
                        label = "Email",
                        supportingText = "We'll never share your email"
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `error state with error text light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = "invalid!",
                        onValueChange = {},
                        placeholder = "Enter value",
                        label = "Amount",
                        isError = true,
                        supportingText = "Invalid amount entered"
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `error state with error text dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = "invalid!",
                        onValueChange = {},
                        placeholder = "Enter value",
                        label = "Amount",
                        isError = true,
                        supportingText = "Invalid amount entered"
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `disabled state light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = "Read only",
                        onValueChange = {},
                        placeholder = "Placeholder",
                        enabled = false
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `disabled state dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    FinsibleTextField(
                        value = "Read only",
                        onValueChange = {},
                        placeholder = "Placeholder",
                        enabled = false
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `stacked fields light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                        FinsibleTextField(value = "John", onValueChange = {}, placeholder = "First name", label = "First Name")
                        FinsibleTextField(value = "Doe", onValueChange = {}, placeholder = "Last name", label = "Last Name")
                        FinsibleTextField(value = "john@example.com", onValueChange = {}, placeholder = "Email", label = "Email")
                    }
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `stacked fields dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 320.dp)) {
                    Column(verticalArrangement = androidx.compose.foundation.layout.Arrangement.spacedBy(12.dp)) {
                        FinsibleTextField(value = "John", onValueChange = {}, placeholder = "First name", label = "First Name")
                        FinsibleTextField(value = "Doe", onValueChange = {}, placeholder = "Last name", label = "Last Name")
                        FinsibleTextField(value = "john@example.com", onValueChange = {}, placeholder = "Email", label = "Email")
                    }
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
