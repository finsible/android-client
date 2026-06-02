package com.itsjeel01.finsiblefrontend.ui.navigation

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class NavigationFlowTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun app_can_render_a_test_button() {
        composeTestRule.setContent {
            com.itsjeel01.finsiblefrontend.ui.theme.FinsibleTheme {
                com.itsjeel01.finsiblefrontend.ui.component.templates.component.FinsibleButton(
                    onClick = {},
                    text = "Hello App"
                )
            }
        }

        composeTestRule.waitForIdle()
        composeTestRule.onNodeWithText("Hello App").assertExists()
    }
}
