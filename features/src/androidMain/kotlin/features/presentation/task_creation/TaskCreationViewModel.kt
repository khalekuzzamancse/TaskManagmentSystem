package features.presentation.task_creation

import androidx.lifecycle.ViewModel
import core.logic.FeedbackController
import core.logic.FeedbackControllerImpl
import features.data.TaskRepositoryImpl
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import features.presentation_logic.TaskWriteController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class TaskCreationViewModel :TaskWriteControllerTemplate() {
     val tag = "TaskCreationViewModel"
    private val repository = TaskRepositoryImpl()
     override suspend fun writeOrThrow() {
         repository.createOrThrow(task.value)
     }
}
