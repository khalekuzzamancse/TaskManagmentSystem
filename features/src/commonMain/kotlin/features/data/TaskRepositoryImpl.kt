@file:Suppress("FunctionName")

package features.data

import core.data.api.TaskEntity
import core.data.room.ApiFactory
import features.domain.PriorityModel
import features.domain.StatusModel
import features.domain.TaskModel
import features.domain.TaskRepository

class TaskRepositoryImpl : TaskRepository {
    private val api = ApiFactory.createTaskApiOrThrow()
    override suspend fun createOrThrow(model: TaskModel) {
        api.createOrThrow(model._toEntity())
    }
    override suspend fun readOrThrow(id: String): TaskModel {
        return api.readOrThrow(id)._toModel()
    }

    override suspend fun readTasksOrThrow(): List<TaskModel> {
        return api.readTasksOrThrow().map {
           it._toModel()
        }
    }
    override suspend fun updateOrThrow(model: TaskModel) {
        api.updateOrThrow(model._toEntity())
    }

    override suspend fun searchOrThrow(query: String): List<TaskModel> {
        return api.searchOrThrow(query).map {
            it._toModel()
        }
    }

    override suspend fun deleteOrThrow(id: String) {
        api.deleteOrThrow(id)
    }
    private fun TaskEntity._toModel(): TaskModel {
        return TaskModel(
            title = this.title,
            description = this.description,
            dueDate = this.dueDate,
            priority = PriorityModel.entries[this.priority],
            status = StatusModel.entries[this.status],
            createdOn = this.createdOn,
            id = this.id
        )
    }
    private fun TaskModel._toEntity(): TaskEntity {
        return TaskEntity(
            title = this.title,
            description = this.description,
            dueDate = this.dueDate,
            priority = this.priority.ordinal,
            status = this.status.ordinal,
            createdOn = this.createdOn)
    }
}