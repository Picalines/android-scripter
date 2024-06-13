package com.picalines.scripter

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import com.kaspersky.kaspresso.testcases.api.testcase.TestCase
import org.junit.Rule
import org.junit.Test
import kotlin.random.Random

class LoginScreenTest : TestCase() {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun hasElements() {
        composeTestRule.onNodeWithTag("email").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("password").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("login").assertExists().assertIsDisplayed()
        composeTestRule.onNodeWithTag("register").assertExists().assertIsDisplayed()
    }
}