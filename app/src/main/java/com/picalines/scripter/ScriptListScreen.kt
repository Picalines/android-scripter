package com.picalines.scripter

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.picalines.scripter.data.TestingScriptSource
import com.picalines.scripter.domain.Script
import com.picalines.scripter.ui.theme.CodeBg0Soft
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScriptListScreen(modifier: Modifier = Modifier) {
    Surface(modifier = modifier, color = CodeBg0Soft) {
        KeywordsBackground(
            modifier = Modifier.fillMaxSize(),
            textStyle = MaterialTheme.typography.headlineLarge,
            rowSpacing = 50.dp,
            numberOfRows = 10
        )
        Column(
            modifier = Modifier.background(color = CodeBg0Soft.copy(alpha = 0.8f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                ) {
                    Text(
                        text = "Scripts",
                        color = Color.White,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = {}) {
                        Icon(
                            imageVector = Icons.Filled.AddCircle,
                            tint = Color.White,
                            contentDescription = "Create Script"
                        )
                    }
                }

                LazyColumn {
                    items(TestingScriptSource().getScripts()) { script ->
                        ScriptCard(script)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ScriptCard(script: Script, modifier: Modifier = Modifier) {
    script.toMap()
    Card(
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp)
                .wrapContentHeight(), verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = script.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${if (script.updatedAt != null) "Updated" else "Created"} ${
                        (script.updatedAt ?: script.createdAt).format(
                            DateTimeFormatter.ofPattern("dd-MM-yyyy")
                        )
                    }"
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Column {
                Row {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.PlayArrow, contentDescription = "Run Script"
                        )
                    }
                    IconButton(onClick = { }) {
                        Icon(imageVector = Icons.Filled.Edit, contentDescription = "Edit Script")
                    }
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete Script"
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
private fun ScriptListScreenPreview() {
    ScriptListScreen(Modifier.fillMaxSize())
}