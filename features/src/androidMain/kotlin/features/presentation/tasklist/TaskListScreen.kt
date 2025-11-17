package features.presentation.tasklist

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import core.ui.FilterBottomSheetPreview
import core.ui.IconButtonView
import core.ui.NoDataView
import core.ui.ScreenStrategy
import core.ui.SearchBar
import core.ui.SpacerHorizontal
import core.ui.SpacerVertical
import core.ui.TextHeading2
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit,
    navRail: @Composable () -> Unit,
    fab: @Composable () -> Unit,
    onDetailsRequest: (String) -> Unit,
) {

    val viewModel = viewModel { TaskListViewModel() }
    LaunchedEffect(Unit) {
        viewModel.read()
    }
    val tasks = viewModel.tasks.collectAsState().value
    var showDialog by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskModel?>(null) }
    val isLoading = viewModel.isLoading.collectAsState().value
    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    if (showDialog && selectedTask != null) {
        DeleteConfirmationDialog(
            onConfirmDelete = {
                // Handle task deletion logic
                selectedTask?.let {
                    viewModel.delete(it.id)
                }
                showDialog = false
            },
            onDismiss = { showDialog = false } // Close dialog if dismissed
        )
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false

            },
            sheetState = sheetState
        ) {
            FilterBottomSheetPreview()
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
                        icon = Icons.Filled.FilterAlt,
                    ) {
                        showBottomSheet = true
                    }
                    SpacerHorizontal(8)
                    IconButtonView(
                        icon = Icons.AutoMirrored.Filled.Sort
                    ) { }



                }
            )
        }

    ) { modifier ->
        Column {
            SearchBar(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onSearch = { query ->
                    viewModel.search(query)

                }
            )
            SpacerVertical(24)
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
    LazyColumn(modifier = Modifier.fillMaxSize()) {
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
        }
    }
}

@Composable
fun TaskItem(task: TaskModel, onClick: () -> Unit, onLongClick: () -> Unit) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .combinedClickable(
                onClick = { onClick() }, // Action for regular click
                onLongClick = { onLongClick() } // Action for long click
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder for Task Icon (representing the task status)
        StatusIcon(status = task.status)

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = task.title,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formatTime(task.createdOn),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        // Right Arrow indicating next action
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = "Next",
            modifier = Modifier.size(20.dp),
            tint = Color.Gray
        )
    }
}

@Composable
fun StatusIcon(status: StatusModel) {
    val color = when (status.ordinal) {
        1 -> Color.Gray // TODO
        2 -> Color.Blue // IN_PROGRESS
        3 -> Color.Green // DONE
        else -> Color.Black // Default
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "T", // Placeholder for icon
            color = Color.White,
            fontSize = 20.sp
        )
    }
}

fun formatTime(timestamp: Long): String {
    val sdf = SimpleDateFormat("hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

@Preview(showBackground = true)
@Composable
fun TaskScreenPreview() {
    val tasks = listOf(
        TaskModel(
            "UI Design",
            "Designing UI for new app",
            1636048511000,
            PriorityModel.LOW,
            StatusModel.TODO,
            System.currentTimeMillis(),
            "4"
        ),
    )
    TaskScreen(tasks = tasks, onLongClick = {}, onDetailsRequest = {})

}
