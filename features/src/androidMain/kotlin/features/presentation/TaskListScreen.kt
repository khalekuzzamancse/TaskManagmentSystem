package features.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.ui.ScreenStrategy
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun TaskListScreen(
    modifier: Modifier = Modifier,
    bottomBar: @Composable () -> Unit,
    navRail: @Composable () -> Unit,
    fab: @Composable () -> Unit
) {
    ScreenStrategy(
        fab = fab,
        bottomBar = bottomBar,
        navRail = navRail,
    ) { modifier ->
        TaskScreenPreview()
    }
}
@Composable
fun TaskScreen(tasks: List<TaskModel>) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(tasks.size) { index ->
            TaskItem(task = tasks[index])
        }
    }
}

@Composable
fun TaskItem(task: TaskModel) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable { /* Handle item click if needed */ },
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
            System.currentTimeMillis()
        ),
        TaskModel(
            "Web Development",
            "Working on web app features",
            1636048511000,
            PriorityModel.HIGH,
            StatusModel.InProgress,
            System.currentTimeMillis()
        ),
        TaskModel(
            "Office Meeting",
            "Team meeting for project",
            1636048511000,
            PriorityModel.MEDIUM,
            StatusModel.DONE,
            System.currentTimeMillis()
        ),
        TaskModel(
            "Dashboard Design",
            "Building the dashboard layout",
            1636048511000,
            PriorityModel.MEDIUM,
            StatusModel.TODO,
            System.currentTimeMillis()
        )
    )
        TaskScreen(tasks = tasks)

}
