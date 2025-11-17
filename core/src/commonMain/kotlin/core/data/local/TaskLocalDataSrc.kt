@file:Suppress("functionName")
package core.data.local

import core.data.api.TaskApi
import core.data.api.TaskEntity
import core.data.room.TaskDao
import core.data.room.TaskSchema
import core.language.CustomException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TaskLocalDataSrc(private val dao: TaskDao) : TaskApi{
    override suspend fun createOrThrow(entity: TaskEntity) { dao.upsert(entity._toSchema()) }
    override suspend fun updateOrThrow(id: String, description: String?, dueDate: Long?, priority: Int?, status: Int?) {
        val id=id._idOrThrow()
        val schema = dao.readTask(id)
        if(schema == null) return
        dao.upsert(
            TaskSchema(
                title = schema.title,
                description = description ?: schema.description,
                dueDate = dueDate ?: schema.dueDate,
                priority = priority ?: schema.priority,
                status = status ?: schema.status,
                createdOn = id
            )
        )
    }

    override suspend fun readOrThrow(id: String): TaskEntity {
        val schema = dao.readTask(id._idOrThrow())
        if(schema == null) throw CustomException(message = "Task not found", debugMessage = "TaskLocalDataSrc.readOrThrow")
        return schema._toEntity()

    }
    override  suspend fun deleteOrThrow(id: String) {
        val schema = dao.readTask(id._idOrThrow())
        if(schema == null)
            throw CustomException(message = "Task not found", debugMessage = "TaskLocalDataSrc.readOrThrow")
        dao.deleteTaskById(id._idOrThrow())
    }

    override suspend fun observeTasks(): Flow<List<TaskEntity>> {
       return dao.observerTasks().map {
            it.map { it._toEntity() }
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