package core.ui
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
@Composable
fun FilterControlView(
    modifier: Modifier = Modifier,
    onResetRequest: () -> Unit,
    onApplyRequest: () -> Unit
) {
    Row(modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = "Filter",
            fontSize = 22.sp,
            fontWeight = FontWeight.W400,
            modifier = Modifier.padding(start = 16.dp)
        )
        SpacerFillAvailable()
        TextButton(
            onClick = onResetRequest,
        ) {
            Text(
                text = "Reset All",
            )
        }
        TextButton(
            onClick = onApplyRequest,
        ) {
            Text(
                text = "Apply",
            )
        }

    }
    SpacerVertical(8)
    DividerHorizontal()
    SpacerVertical(8)
    
}


@Composable
fun FilterView(
    modifier: Modifier = Modifier,
    name: String,
    options: List<String>,
    selected: String? = null,
    onSelected: (String) -> Unit,
    groupText: @Composable (Modifier, String) -> Unit = { modifier, group ->
        Text(
            text = group,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            modifier = modifier
        )
    }
) {
    val contentPadding = 8
    Column(modifier = modifier.fillMaxWidth()) {
        groupText(Modifier, name)
        SpacerVertical(16)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.horizontalScroll(rememberScrollState())
        ) {
            SpacerHorizontal(contentPadding)
            options.forEach { option ->
                val selected = selected == option
                val containerColor = if (selected) MaterialTheme.colorScheme.primary else
                    MaterialTheme.colorScheme.secondary
                Surface(
                    modifier = Modifier
                        .then(
                            if (selected)
                                Modifier.border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(16.dp)
                                )
                            else Modifier
                        )
                        .clickable {
                            onSelected(option)
                        },
                    shape = RoundedCornerShape(16.dp),
                    //shadowElevation = 4.dp,
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                    )
                }

            }
            SpacerHorizontal(contentPadding)

        }

    }


}

@Preview
@Composable
fun FilterBottomSheetPreview(
    modifier: Modifier = Modifier
) {
    var selectedRange by remember() {
        mutableStateOf<Pair<Long?, Long?>>(null to null)
    }
    Column(modifier = modifier) {
        FilterControlView(
            modifier = Modifier,
            onResetRequest = { },
            onApplyRequest = { }
        )
        FilterView(
            modifier = Modifier,
            name = "Status",
            options = listOf("Todo", "In Progress", "Done"),
            selected = "",
            onSelected = {},
            groupText = { modifier, group ->
                TextHeading3(text = group, modifier = modifier.padding(start = 16.dp))
            }
        )
        FilterView(
            modifier = Modifier,
            name = "Priority",
            options = listOf("Low", "Medium", "High"),
            selected = "",
            onSelected = {},
            groupText = { modifier, group ->
                TextHeading3(text = group, modifier = modifier.padding(start = 16.dp))
            }
        )
        SpacerVertical(16)
        Column {
            TextHeading3(text = "Date Range", modifier = Modifier.padding(start = 16.dp))
            DateRangeFilter(
                modifier = Modifier.padding(start = 16.dp),
                selectedRange = selectedRange,
                onSelection = {
                    selectedRange = it
                }
            )
        }
    }
}