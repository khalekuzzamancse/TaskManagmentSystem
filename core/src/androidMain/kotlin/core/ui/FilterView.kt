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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Composable

fun FilterView(
    modifier: Modifier = Modifier,
    controller: FilterViewController,
    onResetRequest: () -> Unit
) {
    val contentPadding = 8
    val groups = controller.groups.collectAsState().value
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
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

        }
        SpacerVertical(8)
        DividerHorizontal()
        SpacerVertical(8)
        groups.forEachIndexed { index, group ->
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = group.groupName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    modifier = Modifier.padding(start = 16.dp)
                )
                SpacerVertical(16)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    SpacerHorizontal(contentPadding)
                    group.options.forEach { option ->
                        val selected = group.selected == option
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
                                    controller.onSelected(group.groupName, option)
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
            if (index != groups.lastIndex)
                SpacerVertical(16)
        }
    }


}

interface FilterViewController {
    val groups: StateFlow<List<FilterBottomSheetViewOption>>
    fun onSelected(groupName: String, option: String)
    val selectedByGroup: StateFlow<Map<String, String>>
    fun update(groups: List<FilterBottomSheetViewOption>)
    fun reset()
    fun saveUndoOption()
    fun undo()

    companion object {
        fun create(initialOptions: List<FilterBottomSheetViewOption>): FilterViewController =
            DefaultFilterViewController(initialOptions)
    }

}

data class FilterBottomSheetViewOption(
    val groupName: String,
    val options: List<String>,
    val selected: String? = null
)

class DefaultFilterViewController(
    private val initialOptions: List<FilterBottomSheetViewOption>
) : FilterViewController {
    private var undoOptions = initialOptions

    // Backing state for options
    override val groups = MutableStateFlow(initialOptions)
    override val selectedByGroup = MutableStateFlow<Map<String, String>>(emptyMap())
    override fun update(groups: List<FilterBottomSheetViewOption>) {
        this.groups.update { groups }
    }

    override fun onSelected(groupName: String, option: String) {
        groups.update { groups ->
            groups.map { group ->
                if (group.groupName == groupName)
                    group.copy(selected = option)
                else
                    group
            }
        }
        selectedByGroup.update { groups ->
            groups.toMutableMap().apply {
                put(groupName, option)
            }
        }
    }

    override fun reset() {
        groups.update { initialOptions }
    }

    override fun saveUndoOption() {
        undoOptions = groups.value
    }

    override fun undo() {
        groups.update { undoOptions }
    }
}


@Preview
@Composable
fun FilterBottomSheetPreview() {
    val controller = remember {
        FilterViewController.create(
            listOf(
                FilterBottomSheetViewOption(
                    groupName = "Status",
                    options = listOf("Todo", "In Progress", "Done"),
                ),
                FilterBottomSheetViewOption(
                    groupName = "Priority",
                    options = listOf("Low", "Medium", "High")
                ),
            )
        )
    }
    Column(

    ) {
        FilterView(
            modifier = Modifier,
            controller = controller,
            onResetRequest = {}
        )
        SpacerVertical(16)
        DateRangeFilter(
            modifier = Modifier,
            onSelection = {},
            onClear = {}
        )

    }


}