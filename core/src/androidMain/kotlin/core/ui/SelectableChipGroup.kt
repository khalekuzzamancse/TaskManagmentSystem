package core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun SelectableChipGroup(
    label: String,
    options: List<String>,
    selectedOption: String,
    onOptionSelected: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        TextHeading3(text = label)
        SpacerVertical(16)
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
        ) {
            options.forEach { option ->
                val selected = selectedOption == option
                val containerColor = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceContainer

                Surface(
                    modifier = Modifier
                        .clickable { onOptionSelected(option) }
                        .then(
                            if (selected) Modifier.border(
                                width = 1.dp,
                                color = Color.Blue,
                                shape = RoundedCornerShape(16.dp)
                            )
                            else Modifier
                        ),
                    shape = RoundedCornerShape(16.dp),
                    color = containerColor
                ) {
                    Text(
                        text = option,
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                        color = MaterialTheme.colorScheme.contentColorFor(containerColor)
                    )
                }
            }
        }
    }
}
