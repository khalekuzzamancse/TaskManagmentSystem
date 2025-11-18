package core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
 fun SearchBar(
    modifier: Modifier = Modifier,
    searchHint: String = "Search",
    onSearch: (String?) -> Unit
) {
    val focusManager= LocalFocusManager.current

    var text by rememberSaveable { mutableStateOf("") }
    var debounceText by rememberSaveable { mutableStateOf("") }
    val color= Color(0xFFF6F6F6)
    LaunchedEffect(debounceText) {
        delay(1000)
        val query= debounceText.ifEmpty { null }
        onSearch(query)
      focusManager.clearFocus()

    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .height(48.dp)
            .background(
                color, RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp)),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            tint= MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 8.dp)
        )

        Spacer(Modifier.width(8.dp))

        // Use Box for stacking text and placeholder
        Box(
            Modifier
                .weight(1f)
                .padding(vertical = 0.dp)
        ) {
            BasicTextField(
                value = text,
                onValueChange = {
                    text = it
                    debounceText = it
                                },
                singleLine = true,
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
                    .padding(end = if (text.isNotEmpty()) 38.dp else 0.dp) // so clear icon won't overlap
            )

            // Show the placeholder only if the input is empty
            if (text.isEmpty()) {
                Text(
                    searchHint,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color =Color.Gray,
                    fontSize = 15.sp,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                )
            }
        }

        if (text.isNotEmpty()) {
            CloseIconButton(
                onClick = {
                    text = ""
                    onSearch(null)
                },
                modifier = Modifier
            )
            SpacerHorizontal(4)
        }

    }
}