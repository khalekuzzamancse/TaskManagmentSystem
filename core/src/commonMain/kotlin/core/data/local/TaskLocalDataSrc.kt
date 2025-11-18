@file:Suppress("functionName")
package core.data.local

import core.data.api.TaskApi
import core.data.api.TaskEntity
import core.data.room.TaskDao
import core.data.room.TaskSchema
import core.language.CustomException
import core.language.toDateString
import kotlinx.coroutines.delay

class TaskLocalDataSrc(private val dao: TaskDao) : TaskApi{
    override suspend fun createOrThrow(entity: TaskEntity) {
        //Fake loading
        delay(1000)
        dao.upsert(entity._toSchema())
    }
    override suspend fun updateOrThrow(entity: TaskEntity) {
        //Fake loading
        delay(1000)
        dao.upsert(entity._toSchema())

    }

    override suspend fun readOrThrow(id: String): TaskEntity {
        //Fake loading
        delay(1000)
        val schema = dao.readTask(id._idOrThrow())
        if(schema == null) throw CustomException(message = "Task not found", debugMessage = "TaskLocalDataSrc.readOrThrow")

        return schema._toEntity()

    }
    override  suspend fun deleteOrThrow(id: String) {
        delay(1000)

        val schema = dao.readTask(id._idOrThrow())
        if(schema == null)
            throw CustomException(message = "Task not found", debugMessage = "TaskLocalDataSrc.readOrThrow")
        //Fake loading
        dao.deleteTaskById(id._idOrThrow())
    }

    override suspend fun readTasksOrThrow(): List<TaskEntity> {
        //Fake loading
        delay(1000)
       return dao.readTasksOrThrow().map {
            it._toEntity()
        }
    }

    override suspend fun byPrioritySortOrThrow(): List<TaskEntity> {
        //Fake loading
        delay(1000)
        return  dao.sortByPriority().map { it._toEntity() }
    }

    override suspend fun byStatusSortOrThrow(): List<TaskEntity> {
        //Fake loading
        delay(1000)
        return dao.sortByStatus().map { it._toEntity() }
    }

    override suspend fun byDateSortOrThrow(): List<TaskEntity> {
        //Fake loading
        delay(1000)
        return dao.sortByDate().map { it._toEntity() }
    }

    override suspend fun searchOrThrow(query: String): List<TaskEntity> {
        //Fake loading
        delay(300)
        return dao.searchTasks(query).map {
            it._toEntity()
        }
    }

    override suspend fun filterOrThrow(
        status: Int?,
        priority: Int?,
        dateRange: Pair<Long?, Long?>
    ): List<TaskEntity> {
        //Fake loading
        delay(1000)
        val (start, end) = dateRange

        return when {
            // Case 1: Only status is provided (priority and date are null)
            status != null && priority == null && start == null && end == null -> {
                val tasks = dao.filterStatus(status)

                tasks.map { it._toEntity() }
            }

            // Case 2: Only priority is provided (status and date are null)
            priority != null && status == null && start == null && end == null -> {
                val tasks = dao.filterPriority(priority)

                tasks.map { it._toEntity() }
            }

            // Case 3: Only start date is provided (priority and status are null)
            start != null && priority == null && status == null && end == null -> {
                val tasks = dao.filterDate(start.toDateString())

                tasks.map { it._toEntity() }
            }

            // Case 4: Start and end date are provided (priority and status are null)
            start != null && end != null && priority == null && status == null -> {
                val tasks = dao.filterDate(start.toDateString(), end.toDateString())

                tasks.map { it._toEntity() }
            }

            // Case 5: status and priority are provided (date is null)
            status != null && priority != null && start == null && end == null -> {
                val tasks = dao.filter(status, priority)

                tasks.map { it._toEntity() }
            }

            // Case 6: status, priority, and date are provided
            status != null && priority != null && start != null && end == null -> {
                val tasks = dao.filter(status, priority, start.toDateString())
                tasks.map { it._toEntity() }
            }

            // Case 7: status, priority, and date range are provided
            status != null && priority != null && start != null && end != null -> {
                val tasks = dao.filter(status, priority, start.toDateString(), end.toDateString())
                tasks.map { it._toEntity() }
            }

            // Case 8: If no filters are provided, return all tasks
            else -> {
                val tasks = dao.readTasksOrThrow()
                tasks.map { it._toEntity() }
            }
        }
    }


    private fun TaskSchema._toEntity(): TaskEntity{
        return TaskEntity(
            title = this.title,
            description = this.description,
            dueDate = this.dueTimestamp,
            priority = this.priority,
            status = this.status,
            createdOn = this.createdOn)
    }
    private fun TaskEntity._toSchema(): TaskSchema{
        return TaskSchema(
            title = this.title,
            description = this.description,
            dueTimestamp = this.dueDate,
            priority = this.priority,
            status = this.status,
            createdOn = this.createdOn,
        )
    }
    fun String._idOrThrow(): Long = this.toLongOrNull()?:throw CustomException(message = "Invalid id", debugMessage = "TaskLocalDataSrc.readOrThrow")
}