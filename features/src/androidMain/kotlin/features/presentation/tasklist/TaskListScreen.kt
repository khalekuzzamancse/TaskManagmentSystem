@file:Suppress("ComposableNaming")

package features.presentation.tasklist

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.FilterAlt
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.DateRangeFilter
import core.ui.DateTimeUtils
import core.ui.DividerHorizontal
import core.ui.FilterControlView
import core.ui.FilterView
import core.ui.IconButtonView
import core.ui.NoDataView
import core.ui.ScreenStrategy
import core.ui.SearchBar
import core.ui.SpacerHorizontal
import core.ui.SpacerVertical
import core.ui.TextHeading2
import core.ui.TextHeading3
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import features.presentation_logic.TaskListController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    bottomBar: @Composable () -> Unit,
    navRail: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    onDetailsRequest: (String) -> Unit,
) {
    val viewModel = viewModel { TaskListViewModel() }
    val tasks = viewModel.tasks.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskModel?>(null) }
    val isLoading = viewModel.isLoading.collectAsState().value
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (showDialog && selectedTask != null) {
        DeleteConfirmationDialog(
            onConfirmDelete = {
                selectedTask?.let {
                    viewModel.delete(it.id)
                }
                showDialog = false
            },
            onDismiss = { showDialog = false }
        )
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                viewModel.clearFilter(false)
            },
            sheetState = sheetState
        ) {
            _FilterView(
                controller = viewModel,
                onApplyRequested = {
                    viewModel.filter()
                    showBottomSheet = false
                },
                onResetRequest = {
                    viewModel.clearFilter(true)
                    showBottomSheet = false
                }
            )
        }
    }
    ScreenStrategy(
        fab = fab,
        controller = viewModel,
        bottomBar = bottomBar,
        navRail = navRail,
        topBar = {
            TopAppBar(
                title = {
                    TextHeading2(
                        text = "Task List"
                    )
                },
                actions = {
                    IconButtonView(
                        icon = Icons.Outlined.Refresh,
                        tint = MaterialTheme.colorScheme.primary
                    ) {
                        viewModel.read()
                    }
                    IconButtonView(
                        icon = Icons.Outlined.FilterAlt,
                        tint = MaterialTheme.colorScheme.primary
                    ) {
                        showBottomSheet = true
                    }
                    SpacerHorizontal(2)
                    _SortDropDownMenu(
                        onPrioritySortRequest = viewModel::sortByPriority,
                        onDateSortRequest = viewModel::sortByDate,
                        onStatusSortRequest = viewModel::sortByStatus,
                        controller = viewModel
                    )
                }
            )
        }

    ) { modifier ->
        Column(modifier = modifier) {
            SearchBar(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearch = { query ->
                    viewModel.search(query)
                }
            )
            SpacerVertical(16)
            DividerHorizontal()
            if (tasks.isEmpty() && !isLoading) {
                NoDataView()
            } else {
                TaskScreen(
                    tasks = tasks,
                    onDetailsRequest = onDetailsRequest,
                    onLongClick = {
                        selectedTask = it
                        showDialog = true

                    }
                )
            }

        }

    }
}

@Composable
fun DeleteConfirmationDialog(
    onConfirmDelete: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Task") },
        text = { Text("Are you sure you want to delete this task?") },
        confirmButton = {
            TextButton(onClick = {
                onConfirmDelete()
            }) {
                Text("Yes")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

@Composable
fun TaskScreen(
    tasks: List<TaskModel>,
    onDetailsRequest: (String) -> Unit,
    onLongClick: (TaskModel) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp)
    ) {

        items(tasks.size) { index ->
            TaskItem(
                task = tasks[index],
                onClick = {
                    onDetailsRequest(tasks[index].id)
                },
                onLongClick = {
                    onLongClick(tasks[index])
                }
            )
            if (index != tasks.lastIndex) {
                SpacerVertical(4)
                DividerHorizontal(modifier = Modifier.padding(horizontal = 16.dp))
                SpacerVertical(8)
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskModel, onClick: () -> Unit, onLongClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() }, // Action for regular click
                onLongClick = { onLongClick() } // Action for long click
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {

        StatusIcon(status = task.priority)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (task.dueDate != null) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Timer,
                            contentDescription = "Due Date",
                            tint = Color.Gray,
                            modifier = Modifier.size(20.dp)
                        )
                        SpacerHorizontal(4)
                        Text(
                            text = DateTimeUtils.formatDateInMs(task.dueDate),
                            fontSize = 15.sp,
                            color = Color.Gray
                        )
                    }

                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    text = task.status.label,
                    color = when (task.status) {
                        StatusModel.TODO -> Color(0xFFFF0000)
                        StatusModel.InProgress -> Color(0xFFFFC107)
                        StatusModel.DONE -> Color(0xFF43A047)
                    },
                    modifier = Modifier
                )
            }


        }

    }
}

@Composable
fun StatusIcon(status: PriorityModel) {
    val primary = MaterialTheme.colorScheme.primary
    val color = primary.copy(alpha = 0.6f)

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = when (status) {
                PriorityModel.LOW -> "L"
                PriorityModel.MEDIUM -> "M"
                PriorityModel.HIGH -> "H"
            },
            color = Color.White,
            fontSize = 20.sp
        )
    }
}


@Composable
fun _SortDropDownMenu(
    controller: TaskListController,
    onPrioritySortRequest: () -> Unit,
    onDateSortRequest: () -> Unit,
    onStatusSortRequest: () -> Unit,
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val selected = controller.selectedSortOption.collectAsState().value
    Box(
        modifier = Modifier
            .padding(16.dp)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                Icons.AutoMirrored.Filled.Sort,
                contentDescription = "Sort",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()

                ){
                    Text(text = "Sort By", fontSize = 16.sp)
                    IconButton(
                        onClick = {
                            controller.clearSort()
                            expanded=false
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Clear,
                            contentDescription = "Clear",
                        )
                    }
                }

                SpacerVertical(4)
                DividerHorizontal()
                _DropDownItem(
                    label = "Priority",
                    selected = selected == 1,
                    onClick = {
                        onPrioritySortRequest()
                        expanded = false
                    }

                )
                _DropDownItem(
                    label = "Status",
                    selected = selected == 2,
                    onClick = {
                        onStatusSortRequest()
                        expanded = false
                    }
                    )
                _DropDownItem(
                    label = "Date",
                    selected = selected == 3,
                    onClick = {
                        onDateSortRequest()
                        expanded = false
                    }
                )
            }


        }
    }
}

@Composable
fun _DropDownItem(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    DropdownMenuItem(
        modifier = modifier,
        text = {
            Text(
              text=  label,
                fontWeight = if(selected) FontWeight.W700 else FontWeight.W400
            )
        },
        onClick =onClick
    )

}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    val task = TaskModel(
        "UI Design",
        "Designing UI for new app",
        1636048511000,
        PriorityModel.LOW,
        StatusModel.TODO,
        System.currentTimeMillis(),
        "4"
    )

    TaskItem(
        task = task,
        onClick = {},
        onLongClick = {}
    )

}

@Composable
fun _FilterView(
    modifier: Modifier = Modifier,
    controller: TaskListController,
    onApplyRequested: () -> Unit,
    onResetRequest: () -> Unit,
) {

    val statusOptions = controller.statusOptions
    val priorityOptions = controller.priorityOptions
    val selectedStatus = controller.selectedStatus.collectAsState().value
    val selectedPriority = controller.selectedPriority.collectAsState().value
    val selectedDateRange = controller.selectedDateRange.collectAsState().value

    Column(modifier = modifier) {
        FilterControlView(
            modifier = Modifier,
            onResetRequest = onResetRequest,
            onApplyRequest = onApplyRequested
        )
        FilterView(
            modifier = Modifier,
            name = "Status",
            options = statusOptions,
            selected = selectedStatus?.label,
            onSelected = {
                controller.onStatusSelected(StatusModel.toStatusOrThrow(it))
            },
            groupText = { modifier, group ->
                TextHeading3(text = group, modifier = modifier.padding(start = 16.dp))
            }
        )
        FilterView(
            modifier = Modifier,
            name = "Priority",
            options = priorityOptions,
            selected = selectedPriority?.label,
            onSelected = {
                controller.onPrioritySelected(PriorityModel.toPriorityOrThrow(it))
            },
            groupText = { modifier, group ->
                TextHeading3(text = group, modifier = modifier.padding(start = 16.dp))
            }
        )
        SpacerVertical(16)
        SpacerVertical(16)
        TextHeading3(text = "Date Range", modifier = Modifier.padding(start = 16.dp))
        DateRangeFilter(
            modifier = Modifier.padding(start = 16.dp),
            selectedRange = selectedDateRange,
            onSelection = controller::onDateRangeSelected
        )
    }
}