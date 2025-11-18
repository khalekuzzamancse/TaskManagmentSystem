package features.presentation_logic

import core.logic.FeedbackController
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import kotlinx.coroutines.flow.StateFlow

interface TaskListController : FeedbackController, FilterController {
    val tasks: StateFlow<List<TaskModel>>
    /** 1: Priority; 2: Status; 3: Date*/
    val selectedSortOption: StateFlow<Int?>
    fun clearSort()
    fun read()
    fun delete(id: String)
    fun search(query: String?)
    fun filter()
    fun sortByPriority()
    fun sortByStatus()
    fun sortByDate()
}
interface FilterController{
    val selectedStatus: StateFlow<StatusModel?>
    val selectedPriority: StateFlow<PriorityModel?>
    val selectedDateRange: StateFlow<Pair<Long?, Long?>>
    val statusOptions: List<String>
    val priorityOptions: List<String>
    fun onPrioritySelected(priority: PriorityModel)
    fun onStatusSelected(status: StatusModel)
    fun onDateRangeSelected(dateRange: Pair<Long?, Long?>)
    fun clearFilter(reload: Boolean)
}