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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

@Composable

fun FilterView(
    modifier: Modifier = Modifier,
    controller: FilterViewController,
    onResetRequest: () -> Unit,
    onApplyRequest: () -> Unit,
    groupText: @Composable (Modifier, String) -> Unit = {modifier,group->
        Text(
            text = group,
            fontSize = 16.sp,
            fontWeight = FontWeight.W400,
            modifier =modifier
        )
    }
) {
    val contentPadding = 8
    val groups = controller.groups.collectAsState().value
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth(),
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
        groups.forEachIndexed { index, group ->
            Column(modifier = Modifier.fillMaxWidth()) {
                groupText(Modifier,group.groupName)
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
fun FilterBottomSheetPreview(
    modifier: Modifier = Modifier
) {
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
    Column (modifier = modifier){
        var selectedRange by remember() {
            mutableStateOf<Pair<Long?, Long?>>(null to null)
        }
        FilterView(
            modifier = Modifier,
            controller = controller,
            onResetRequest = {
                controller.reset()
                selectedRange=(null to null)
            },
            onApplyRequest = {},
            groupText = {modifier,group->
                TextHeading3(text = group, modifier = modifier.padding(start = 16.dp))
            }
        )
        SpacerVertical(16)
        Column {
            TextHeading3(text = "Date Range",modifier = Modifier.padding(start = 16.dp))
            DateRangeFilter(
                modifier = Modifier.padding(start = 16.dp),
                selectedRange=selectedRange,
                onSelection = {
                    selectedRange=it
                }
            )
        }
    }
}