package core.data.api

import kotlinx.coroutines.flow.Flow
/**
 * - id is createdOn
 */
interface TaskApi {
    suspend fun createOrThrow(entity: TaskEntity)
    suspend  fun updateOrThrow(
        id: String,
        description: String?=null,
        dueDate: Long?=null,
        priority:Int??=null,
        status: Int?=null
    )
    suspend fun readOrThrow(id: String): TaskEntity
    suspend fun deleteOrThrow(id: String)
    suspend fun observeTasks(): Flow<List<TaskEntity>>
}
data class TaskEntity(
    val title: String,
    val description: String?,
    val dueDate: Long?,
    val priority:Int,
    val status: Int,
    val createdOn: Long,
){
    val id: String=createdOn.toString()
}


