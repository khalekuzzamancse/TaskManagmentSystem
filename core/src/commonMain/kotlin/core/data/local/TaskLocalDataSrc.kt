@file:Suppress("functionName")
package core.data.local

import core.data.api.TaskApi
import core.data.api.TaskEntity
import core.data.room.TaskDao
import core.data.room.TaskSchema
import core.language.CustomException
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

    override suspend fun searchOrThrow(query: String): List<TaskEntity> {
        //Fake loading
        delay(300)
        return dao.searchTasks(query).map {
            it._toEntity()
        }
    }

    private fun TaskSchema._toEntity(): TaskEntity{
        return TaskEntity(
            title = this.title,
            description = this.description,
            dueDate = this.dueDate,
            priority = this.priority,
            status = this.status,
            createdOn = this.createdOn)
    }
    private fun TaskEntity._toSchema(): TaskSchema{
        return TaskSchema(
            title = this.title,
            description = this.description,
            dueDate = this.dueDate,
            priority = this.priority,
            status = this.status,
            createdOn = this.createdOn,
        )
    }
    fun String._idOrThrow(): Long = this.toLongOrNull()?:throw CustomException(message = "Invalid id", debugMessage = "TaskLocalDataSrc.readOrThrow")
}