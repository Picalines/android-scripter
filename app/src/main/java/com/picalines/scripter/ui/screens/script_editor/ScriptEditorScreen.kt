package com.picalines.scripter.ui.screens.script_editor

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.picalines.scripter.SCRIPT_DEFAULT_ID
import com.picalines.scripter.SCRIPT_LIST_SCREEN
import com.picalines.scripter.SCRIPT_SCREEN
import com.picalines.scripter.ui.theme.CodeBg0Hard
import kotlinx.coroutines.delay

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScriptEditorScreen(
    scriptId: String,
    popUp: () -> Unit,
    restartApp: (String) -> Unit,
    viewModel: ScriptEditorViewModel = hiltViewModel()
) {
    val script = viewModel.script.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.initialize(scriptId, restartApp)
    }

    Column {
        Surface(
            color = CodeBg0Hard, modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = { popUp() }) {
                    Icon(
                        imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                IconButton(onClick = { viewModel.executeScript() }) {
                    Icon(
                        imageVector = Icons.Filled.PlayArrow,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }

                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.Filled.MoreVert,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }
        }

        TextField(
            value = script.value.sourceCode,
            onValueChange = viewModel::updateScript,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun ScriptEditorScreenPreview() {
    ScriptEditorScreen(scriptId = SCRIPT_DEFAULT_ID, {}, {})
}