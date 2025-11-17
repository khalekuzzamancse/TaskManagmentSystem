package features.presentation.task_updation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import features.presentation.task_creation.TaskWriteScreen

@Composable
fun TaskUpdateScreen(
    modifier: Modifier = Modifier,
    id: String,
    onBack: () -> Unit
) {
    val viewModel = viewModel { TaskUpdateViewModel(id) }
    TaskWriteScreen(
        modifier = modifier,
        screenTitle = "Update Existing Task",
        actionLabel = "Update Task",
        viewModel = viewModel,
        onBack = onBack
    )
}