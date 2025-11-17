package features.presentation.task_creation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun TaskCreationScreen(
    modifier: Modifier = Modifier, onBack: () -> Unit = {}) {
    val viewModel = viewModel { TaskCreationViewModel() }
    TaskWriteScreen(
        modifier = modifier,
        viewModel=viewModel,
        screenTitle = "Create New Task",
        actionLabel = "Create Task",
        onBack = onBack
    )
}