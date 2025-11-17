package features.presentation.task_creation

import androidx.lifecycle.ViewModel
import core.language.Logger
import core.logic.FeedbackController
import core.logic.FeedbackControllerImpl
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import features.presentation_logic.TaskCreateController
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TaskCreationViewModel : ViewModel(), TaskCreateController, FeedbackController by FeedbackControllerImpl() {
    val tag="TaskCreationViewModel"
    override val task = MutableStateFlow(
        TaskModel(
            title = "",
            description = "",
            priority = PriorityModel.LOW,
            status = StatusModel.TODO,
            dueDate = null,
            createdOn = System.currentTimeMillis()
        )
    )

    override fun onTitleChange(value: String) {
        task.update {
            it.copy(
                title = value
            )
        }
    }

    override fun onDescriptionChange(value: String) {
        task.update {
            it.copy(
                description = value
            )
        }
    }

    override fun onPriorityChange(value: String) {
        task.update {
            it.copy(
                priority = PriorityModel.toPriorityOrThrow(value)
            )
        }
    }

    override fun onStatusChange(value: String) {
        task.update {
            it.copy(
                status = StatusModel.toStatusOrThrow(value)
            )
        }
    }

    override fun onDueDateChange(value: Long?) {
        task.update {
            it.copy(
                dueDate = value
            )
        }
    }

    override suspend fun create(): Boolean {
        try {
            Logger.on(tag,"model:${task.value}")
            startLoading()
            delay(3000)
        } catch (e: Throwable) {

            updateMessage("Something went wrong")
        } finally {
            stopLoading()
        }
        return false
    }
}