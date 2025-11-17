@file:Suppress("unused")
package features.domain
import kotlinx.coroutines.flow.Flow

interface TaskRepository {
    suspend fun createOrThrow(model: TaskModel)
    suspend fun readOrThrow(id: String): TaskModel
    suspend fun observeTasks(): Flow<List<TaskModel>>
    suspend fun updateOrThrow(model: TaskModel)
    suspend fun searchOrThrow(query: String): List<TaskModel>
    suspend fun deleteOrThrow(id: String)
}




