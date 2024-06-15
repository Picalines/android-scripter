package com.picalines.scripter.ui.screens.script_editor

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.picalines.scripter.SCRIPT_DEFAULT_ID
import com.picalines.scripter.ui.theme.CodeBg0Hard
import com.picalines.scripter.ui.theme.CodeGreen
import com.picalines.scripter.ui.theme.CodeGrey1
import com.picalines.scripter.ui.theme.CodeOrange
import com.picalines.scripter.ui.theme.CodeRed

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

                val running by viewModel.running.collectAsState()

                if (running) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(10.dp)
                    )
                } else {
                    val focusManager = LocalFocusManager.current

                    IconButton(onClick = {
                        focusManager.clearFocus(true)
                        viewModel.executeScript()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                }
            }
        }

        TextField(
            value = script.value.sourceCode,
            onValueChange = viewModel::updateScript,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            visualTransformation = LuaVisualTransformation()
        )

        val scriptOutput by viewModel.scriptOutput.collectAsState()
        val keyboard by keyboardAsState()

        if (scriptOutput.isNotEmpty() && keyboard != Keyboard.Opened) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(text = scriptOutput)
            }
        }

        val requestingInput by viewModel.requestingInput.collectAsState()

        if (requestingInput) {
            val inputPrompt by viewModel.inputPrompt.collectAsState()
            val currentInput by viewModel.currentInput.collectAsState()

            val focusManager = LocalFocusManager.current
            val focusRequester = remember { FocusRequester() }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Text(
                    text = inputPrompt, modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                )
                TextField(
                    value = currentInput,
                    onValueChange = viewModel::updateInput,
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester)
                )
                Button(
                    onClick = {
                        focusManager.clearFocus(true)
                        viewModel.submitInput()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RectangleShape
                ) {
                    Text(text = "Submit")
                }
            }

            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}

class LuaVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        return TransformedText(
            buildHighlightedString(text),
            OffsetMapping.Identity
        )
    }

    private data class Highlight(val regex: Regex, val color: Color)

    companion object {
        private val highlights: Array<Highlight> = arrayOf(
            Highlight(Regex("""\bif\b"""), CodeRed),
            Highlight(Regex("""\band\b"""), CodeRed),
            Highlight(Regex("""\bbreak\b"""), CodeRed),
            Highlight(Regex("""\bdo\b"""), CodeRed),
            Highlight(Regex("""\belse\b"""), CodeRed),
            Highlight(Regex("""\belseif\b"""), CodeRed),
            Highlight(Regex("""\bend\b"""), CodeRed),
            Highlight(Regex("""\bfalse\b"""), CodeRed),
            Highlight(Regex("""\bfor\b"""), CodeRed),
            Highlight(Regex("""\bfunction\b"""), CodeRed),
            Highlight(Regex("""\bif\b"""), CodeRed),
            Highlight(Regex("""\bin\b"""), CodeRed),
            Highlight(Regex("""\blocal\b"""), CodeRed),
            Highlight(Regex("""\bnil\b"""), CodeRed),
            Highlight(Regex("""\bnot\b"""), CodeRed),
            Highlight(Regex("""\bor\b"""), CodeRed),
            Highlight(Regex("""\brepeat\b"""), CodeRed),
            Highlight(Regex("""\breturn\b"""), CodeRed),
            Highlight(Regex("""\bthen\b"""), CodeRed),
            Highlight(Regex("""\btrue\b"""), CodeRed),
            Highlight(Regex("""\buntil\b"""), CodeRed),
            Highlight(Regex("""\bwhile\b"""), CodeRed),
            Highlight(Regex("""\d+(\.\d*)?"""), CodeGreen),
            Highlight(Regex("""".*?""""), CodeOrange),
            Highlight(Regex("""'.*?'"""), CodeOrange),
            Highlight(Regex("""--.*"""), CodeGrey1),
        )
    }

    private fun buildHighlightedString(code: AnnotatedString): AnnotatedString {
        return buildAnnotatedString {
            append(code)

            var index = 0
            while (index < code.text.length) {
                highlights.firstNotNullOfOrNull { highlight ->
                    highlight.regex.matchAt(code, index)?.let { match ->
                        addStyle(
                            SpanStyle(color = highlight.color),
                            index,
                            match.range.last + 1
                        )
                        index += match.value.length
                    }
                }

                index++
            }
        }
    }
}

@Preview
@Composable
fun ScriptEditorScreenPreview() {
    ScriptEditorScreen(scriptId = SCRIPT_DEFAULT_ID, {}, {})
}