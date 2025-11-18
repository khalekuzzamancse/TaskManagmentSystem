package features.presentation.tasklist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.language.Logger
import core.logic.FeedbackController
import core.logic.FeedbackControllerImpl
import features.data.TaskRepositoryImpl
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import features.presentation_logic.TaskListController
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TaskListViewModel : ViewModel(), TaskListController,
    FeedbackController by FeedbackControllerImpl() {
    private val repository = TaskRepositoryImpl.create()
    override val tasks = MutableStateFlow<List<TaskModel>>(emptyList())
    private val tag = "TaskListViewModel"
    override fun read() {
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value = repository.readTasksOrThrow()
            } catch (e: Exception) {

            } finally {
                stopLoading()
            }

        }
    }

    override fun delete(id: String) {
        //Write operation, cancel if already processing
        if (proccessing()) {
            updateMessage("Already processing, try again later")
        }
        viewModelScope.launch {
            try {
                startLoading()
                repository.deleteOrThrow(id)
                read()
            } catch (e: Exception) {

            } finally {
                stopLoading()
            }

        }
    }

    override fun search(query: String?) {
        if (proccessing()) {
            updateMessage("Already processing, try again later")
            return
        }
        if (query.isNullOrEmpty()) {
            read()
            return
        }
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value=repository.searchOrThrow(query)
            } catch (e: Exception) {

            } finally {
                stopLoading()
            }

        }
    }
    override fun filter(
        status: String?,
        priority: String?,
        dateRange: Pair<Long?, Long?>
    ) {
        if (proccessing()) {
            updateMessage("Already processing, try again later")
            return
        }
        viewModelScope.launch {
            try {
                startLoading()
                tasks.value=repository.filterOrThrow(
                    status = status?.let { StatusModel.valueOf(it) },
                    priority = priority?.let { PriorityModel.valueOf(it)},
                    dateRange = dateRange
                )
                Logger.off(tag,"filter: $status, $priority, $dateRange")
            } catch (e: Exception) {

            } finally {
                stopLoading()
            }

        }
    }
}