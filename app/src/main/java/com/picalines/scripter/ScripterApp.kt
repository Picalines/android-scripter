package com.picalines.scripter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.picalines.scripter.ui.screens.login.LoginScreen
import com.picalines.scripter.ui.screens.register.RegisterScreen
import com.picalines.scripter.ui.screens.script_editor.ScriptEditorScreen
import com.picalines.scripter.ui.screens.script_list.ScriptListScreen
import com.picalines.scripter.ui.theme.AndroidScripterTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScripterApp() {
    AndroidScripterTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val appState = rememberAppState()

            NavHost(navController = appState.navController, startDestination = LOGIN_SCREEN) {
                scripterGraph(appState)
            }
        }
    }
}


@Composable
fun rememberAppState(navController: NavHostController = rememberNavController()) =
    remember(navController) {
        ScripterAppState(navController)
    }

@RequiresApi(Build.VERSION_CODES.O)
fun NavGraphBuilder.scripterGraph(appState: ScripterAppState) {
    composable(SCRIPT_LIST_SCREEN) {
        ScriptListScreen(openScreen = { route -> appState.navigate(route) })
    }

    composable(
        route = "$SCRIPT_SCREEN$SCRIPT_ID_ARG",
        arguments = listOf(navArgument(SCRIPT_ID) { defaultValue = SCRIPT_DEFAULT_ID })
    ) {
        ScriptEditorScreen(scriptId = it.arguments?.getString(SCRIPT_ID) ?: SCRIPT_DEFAULT_ID,
            popUp = { appState.popUp() },
            restartApp = { route -> appState.clearAndNavigate(route) }
        )
    }

    composable(LOGIN_SCREEN) {
        LoginScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }

    composable(REGISTER_SCREEN) {
        RegisterScreen(openAndPopUp = { route, popUp -> appState.navigateAndPopUp(route, popUp) })
    }
}