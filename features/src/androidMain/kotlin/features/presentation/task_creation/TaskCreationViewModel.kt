package features.presentation.task_creation

import features.data.TaskRepositoryImpl

class TaskCreationViewModel :TaskWriteControllerTemplate() {
     val tag = "TaskCreationViewModel"
    private val repository = TaskRepositoryImpl.create()
     override suspend fun writeOrThrow() {
         repository.createOrThrow(task.value)
     }
}
