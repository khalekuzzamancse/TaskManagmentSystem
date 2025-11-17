@file:Suppress("ComposableNaming")

package features.presentation.task_creation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.kzcse.hilsadetector.feature._core.presentation.AppTheme
import core.ui.BackIcon
import core.ui.ButtonView
import core.ui.DatePickerView
import core.ui.DescriptionField
import core.ui.ScreenStrategy
import core.ui.SelectableChipGroup
import core.ui.SpacerVertical
import core.ui.TextFieldView
import core.ui.TextHeading2
import core.ui.TextHeading3
import core.ui.screenPaddingAll
import features.domain.PriorityModel
import features.domain.StatusModel
import features.presentation_logic.TaskWriteController
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskWriteScreen(
    modifier: Modifier = Modifier,
    viewModel: TaskWriteController,
    screenTitle: String,
    actionLabel: String,
    onBack: () -> Unit,
) {
    val selectedStatus = viewModel.task.collectAsState().value.status.label
    val selectedPriority = viewModel.task.collectAsState().value.priority.label
    val title = viewModel.task.collectAsState().value.title
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val isLoading = viewModel.isLoading.collectAsState().value
    ScreenStrategy(
        controller = viewModel,
        fab = {},
        bottomBar = {},
        navRail = {},
        title = {
            TextHeading2(text = screenTitle)
        },
        navigationIcon = {
            BackIcon(onClick = onBack)
        },
    ) { modifier ->
        Column(
            modifier = modifier
                .screenPaddingAll()
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            _TitleInputFiled(controller = viewModel)
            _SpacerSibling()
            SelectableChipGroup(
                label = "Priority",
                options = PriorityModel.entries.map { it.label },
                selectedOption = selectedPriority,
                onOptionSelected = viewModel::onPriorityChange
            )
            _SpacerSibling()
            SelectableChipGroup(
                label = "Status",
                options = StatusModel.entries.map { it.label },
                selectedOption = selectedStatus,
                onOptionSelected = viewModel::onStatusChange
            )
            _SpacerSibling()
            _DatePick(controller = viewModel)
            _SpacerSibling()
            _DescriptionField(controller = viewModel)
            SpacerVertical(32)
            ButtonView(
                label = actionLabel,
                disable = title.isBlank() || isLoading,
                modifier = Modifier.fillMaxWidth(),
            ) {
                focusManager.clearFocus()
                scope.launch {
                   viewModel.write()
                }

            }
        }
    }

}

@Composable
fun _TitleInputFiled(
    modifier: Modifier = Modifier,
    controller: TaskWriteController
) {
    val title = controller.task.collectAsState().value.title
    TextHeading3(text = "Title")
    SpacerVertical(16)
    TextFieldView(
        modifier = modifier.fillMaxWidth(),
        value = title,
        singleLine = true,
        onValueChange = controller::onTitleChange
    )
}

@Composable
fun _DescriptionField(
    modifier: Modifier = Modifier,
    controller: TaskWriteController
) {
    val description = controller.task.collectAsState().value.description
    TextHeading3(text = "Description")
    SpacerVertical(16)
    DescriptionField(
        modifier = modifier
            .height(100.dp)
            .fillMaxWidth(),
        value = description ?: "",
        onValueChange = controller::onDescriptionChange
    )
}


@Composable
fun _DatePick(modifier: Modifier = Modifier,controller: TaskWriteController) {
    val date= controller.task.collectAsState().value.dueDate


    TextHeading3(text = "Due Date")
    SpacerVertical(16)
    DatePickerView(
        modifier = modifier.fillMaxWidth(),
        initial = date,
        onDateSelected = controller::onDueDateChange
    )
}

@Composable
fun _SpacerSibling() {
    SpacerVertical(24)
}

@Preview(
    showBackground = true
)
@Composable
fun TaskCreationScreenPreview() {
    AppTheme {
        TaskCreationScreen(

        )
    }

}
