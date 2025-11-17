package core.ui

import android.text.TextPaint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicText
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    CircularProgressIndicator(Modifier.size(64.dp))
}

@Composable
fun TextDescription(
    modifier: Modifier = Modifier,
    text: String,
    textAlign: TextAlign = TextAlign.Justify
) {
    Text(
        text = text,
        fontSize = 16.sp,
        textAlign = textAlign,
        color = MaterialTheme.colorScheme.onBackground,
        modifier = modifier
    )
}
@Composable
fun TextHeading1(
    modifier: Modifier = Modifier,
    text:String,
    textAlign: TextAlign = TextAlign.Start
)
{
    Text(
        modifier = modifier,
        text = text,
        fontSize = 26.sp,
        fontWeight = FontWeight.W600,
        color = MaterialTheme.colorScheme.primary,
        textAlign = textAlign
    )
}

@Composable
fun TextHeading2(
    modifier: Modifier = Modifier,
    text:String,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.W500,
        textAlign = textAlign,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )

}
@Composable
fun TextHeading3(
    modifier: Modifier = Modifier,
    text:String,
    textAlign: TextAlign = TextAlign.Start
) {
    Text(
        text = text,
        fontSize = 22.sp,
        fontWeight = FontWeight.W500,
        textAlign = textAlign,
        color = MaterialTheme.colorScheme.primary,
        modifier = modifier
    )

}

@Composable
fun TextPoint(
    modifier: Modifier = Modifier,
    text: String
) {
    val bulletSize = 8.dp
    val textStyle = TextStyle(
        fontSize = 16.sp,
        lineHeight = 20.sp,
        color = MaterialTheme.colorScheme.onBackground,
        textAlign = TextAlign.Justify
    )

    // measure single-line text height in pixels
    val density = LocalDensity.current
    val paint = remember(textStyle) {
        TextPaint().apply {
            textSize = with(density) { textStyle.fontSize.toPx() }
        }
    }
    val fontMetrics = paint.fontMetrics
    val singleLineHeight = fontMetrics.descent - fontMetrics.ascent

    val shiftDown = with(density) { (singleLineHeight - bulletSize.toPx()) / 2 }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .offset(y = with(density) { shiftDown.toDp() }) // 👈 shift bullet down
                .size(bulletSize)
                .background(MaterialTheme.colorScheme.primary, CircleShape)
        )

        Spacer(modifier = Modifier.width(8.dp))

        BasicText(
            text = text,
            style = textStyle
        )
    }
}



