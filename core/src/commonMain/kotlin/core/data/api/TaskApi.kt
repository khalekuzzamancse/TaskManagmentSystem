package core.data.api

/**
 * - id is createdOn
 */
interface TaskApi {
    suspend fun createOrThrow(entity: TaskEntity)
    suspend  fun updateOrThrow(entity: TaskEntity)
    suspend fun readOrThrow(id: String): TaskEntity
    suspend fun deleteOrThrow(id: String)
    suspend fun readTasksOrThrow():List<TaskEntity>

    suspend fun byPrioritySortOrThrow():List<TaskEntity>
    suspend fun byStatusSortOrThrow():List<TaskEntity>
    suspend fun byDateSortOrThrow():List<TaskEntity>

    suspend  fun searchOrThrow(query: String): List<TaskEntity>
    suspend fun filterOrThrow(status: Int?, priority: Int?, dateRange: Pair<Long?, Long?>): List<TaskEntity>

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


