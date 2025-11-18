@file:Suppress("unused")
package features.domain

interface TaskRepository {
    suspend fun createOrThrow(model: TaskModel)
    suspend fun readOrThrow(id: String): TaskModel
    suspend fun readTasksOrThrow(): List<TaskModel>
    suspend fun updateOrThrow(model: TaskModel)
    suspend fun searchOrThrow(query: String): List<TaskModel>
    suspend fun deleteOrThrow(id: String)
    suspend fun filterOrThrow(status: StatusModel?, priority: PriorityModel?, dateRange: Pair<Long?, Long?>): List<TaskModel>
    suspend fun prioritySortOrThrow():List<TaskModel>
    suspend fun byStatusSortOrThrow():List<TaskModel>
    suspend fun byDateSortOrThrow():List<TaskModel>
}




