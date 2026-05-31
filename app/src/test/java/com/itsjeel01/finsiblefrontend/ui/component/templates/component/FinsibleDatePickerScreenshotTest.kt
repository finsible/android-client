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
import com.itsjeel01.finsiblefrontend.ui.component.templates.model.CalendarConstraints
import com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode
import java.time.LocalDate
import java.time.YearMonth

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34], application = android.app.Application::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
class FinsibleDatePickerScreenshotTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `default picker no selection light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 360.dp)) {
                    FinsibleDatePicker(
                        onDateSelected = {},
                        selectedDate = null,
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `default picker no selection dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 360.dp)) {
                    FinsibleDatePicker(
                        onDateSelected = {},
                        selectedDate = null,
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `picker with selected date light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 360.dp)) {
                    FinsibleDatePicker(
                        onDateSelected = {},
                        selectedDate = LocalDate.of(2025, 6, 15),
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `picker with selected date dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 360.dp)) {
                    FinsibleDatePicker(
                        onDateSelected = {},
                        selectedDate = LocalDate.of(2025, 6, 15),
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `picker constrained to single month light`() {
        composeTestRule.setContent {
            FinsibleTheme {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 360.dp)) {
                    FinsibleDatePicker(
                        onDateSelected = {},
                        selectedDate = null,
                        constraints = CalendarConstraints(
                            startMonth = YearMonth.of(2025, 6),
                            endMonth = YearMonth.of(2025, 6),
                        )
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }

    @Test
    fun `picker constrained to single month dark`() {
        composeTestRule.setContent {
            FinsibleTheme(isDarkTheme = true) {
                Box(Modifier.background(FinsibleTheme.colors.surfaceDefault).padding(16.dp).widthIn(max = 360.dp)) {
                    FinsibleDatePicker(
                        onDateSelected = {},
                        selectedDate = null,
                        constraints = CalendarConstraints(
                            startMonth = YearMonth.of(2025, 6),
                            endMonth = YearMonth.of(2025, 6),
                        )
                    )
                }
            }
        }
        composeTestRule.onRoot().captureRoboImage()
    }
}
