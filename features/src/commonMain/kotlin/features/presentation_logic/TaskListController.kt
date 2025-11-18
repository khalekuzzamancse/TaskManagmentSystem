package features.presentation_logic

import core.logic.FeedbackController
import features.domain.TaskModel
import kotlinx.coroutines.flow.StateFlow

interface TaskListController: FeedbackController {
    val tasks: StateFlow<List<TaskModel>>
    fun read()
    fun delete(id: String)
    fun search(query: String?)
    fun filter(status: String?, priority: String?, dateRange: Pair<Long?, Long?>)
}