package features.presentation.task_creation

import androidx.lifecycle.ViewModel
import core.language.Logger
import core.logic.FeedbackController
import core.logic.FeedbackControllerImpl
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import features.presentation_logic.TaskWriteController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

abstract class TaskWriteControllerTemplate : ViewModel(), TaskWriteController,
    FeedbackController by FeedbackControllerImpl() {
        private val tag = "TaskWriteControllerTemplate"
    abstract suspend  fun writeOrThrow()
    override val task = MutableStateFlow(
        TaskModel(
            title = "",
            description = "",
            priority = PriorityModel.LOW,
            status = StatusModel.TODO,
            dueDate = null,
            createdOn = System.currentTimeMillis(),
            id = "Will decided by the data-src"
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

    override suspend fun write(): Boolean {
        try {
            startLoading()
            Logger.off(tag, "write:${task.value}")
            writeOrThrow()
            return true
        } catch (e: Throwable) {
            onException(e)
            return false
        } finally {
            stopLoading()
        }
    }

}