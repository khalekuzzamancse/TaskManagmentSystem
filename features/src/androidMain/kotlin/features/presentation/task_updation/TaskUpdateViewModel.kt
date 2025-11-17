package features.presentation.task_updation
import androidx.lifecycle.viewModelScope
import core.language.Logger
import features.data.TaskRepositoryImpl
import features.presentation.task_creation.TaskWriteControllerTemplate
import kotlinx.coroutines.launch

class TaskUpdateViewModel(private val id: String) : TaskWriteControllerTemplate(){
    private val repository = TaskRepositoryImpl()
     val tag = "TaskUpdateViewModel"
    init {
        viewModelScope.launch {
            try {
                startLoading()
                task.value = repository.readOrThrow(id)
                Logger.on(tag,"taskRead:${task.value}")
            }
            catch (e: Throwable) {

                updateMessage("Something went wrong")
            }
            finally {
                stopLoading()
            }
        }
    }
    override suspend fun writeOrThrow() {
        repository.updateOrThrow(task.value)
    }

}