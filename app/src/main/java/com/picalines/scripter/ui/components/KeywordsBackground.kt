package com.picalines.scripter.ui.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.picalines.scripter.ui.theme.CodeGreen
import com.picalines.scripter.ui.theme.CodeGrey2
import com.picalines.scripter.ui.theme.CodeOrange
import com.picalines.scripter.ui.theme.CodeRed
import com.picalines.scripter.ui.theme.CodeYellow
import kotlin.random.Random

data class Keyword(val value: String, val color: Color)

val keywords = arrayOf(
    Keyword("function", CodeRed),
    Keyword("local", CodeRed),
    Keyword("end", CodeRed),
    Keyword("if", CodeRed),
    Keyword("for", CodeRed),
    Keyword("while", CodeRed),
    Keyword("then", CodeRed),
    Keyword("print", CodeYellow),
    Keyword("pairs", CodeYellow),
    Keyword("var", CodeGrey2),
    Keyword("0", CodeGreen),
    Keyword("9", CodeGreen),
    Keyword("\"text\"", CodeOrange),
    Keyword("\"lua\"", CodeOrange),
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun KeywordsBackground(
    modifier: Modifier = Modifier,
    numberOfRows: Int = 10,
    keywordsInRow: Int = 15,
    rowSpacing: Dp = 10.dp,
    textStyle: TextStyle = LocalTextStyle.current
) {
    fun buildColoredRow(): AnnotatedString {
        val builder = AnnotatedString.Builder()

        for (i in (1..keywordsInRow)) {
            val keyword = keywords[Random.nextInt(keywords.size)]
            builder.withStyle(
                style = SpanStyle(
                    color = keyword.color
                )
            ) {
                append(keyword.value + " " + " ".repeat(Random.nextInt(4)))
            }
        }

        return builder.toAnnotatedString()
    }

    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(rowSpacing)) {
        for (i in 0..numberOfRows) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = buildColoredRow(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.basicMarquee(delayMillis = 0),
                    style = textStyle
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun KeywordsBackgroundPreview() {
    KeywordsBackground(modifier = Modifier.fillMaxSize())
}