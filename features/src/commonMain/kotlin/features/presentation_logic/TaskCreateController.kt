package features.presentation_logic

import core.logic.FeedbackController
import features.domain.TaskModel
import kotlinx.coroutines.flow.StateFlow

interface TaskCreateController: FeedbackController {
    val task: StateFlow<TaskModel>
    fun onTitleChange(value: String)
    fun onDescriptionChange(value: String)
    fun onPriorityChange(value: String)
    fun onStatusChange(value: String)
    fun onDueDateChange(value: Long?)
    /**
     * @return true if the task was created successfully, false otherwise
     * If creation is failed will update the feedback message
     */
    suspend fun create(): Boolean
}