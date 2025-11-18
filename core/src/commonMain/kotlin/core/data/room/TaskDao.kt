package core.data.room
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import core.language.toDateString

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: TaskSchema)
    @Query("SELECT * FROM TaskSchema WHERE createdOn = :id LIMIT 1")
    suspend fun readTask(id: Long): TaskSchema?
    @Query("DELETE FROM TaskSchema WHERE createdOn = :id")
    suspend fun deleteTaskById(id: Long)
    @Query("SELECT * FROM TaskSchema")
    suspend fun readTasksOrThrow(): List<TaskSchema>
    // Search for tasks by title (or other fields like description)
    @Query("SELECT * FROM TaskSchema WHERE title LIKE :query")
    suspend fun searchTasks(query: String): List<TaskSchema>

    @Query("SELECT * FROM TaskSchema WHERE status = :status")
    suspend fun filterStatus(status: Int): List<TaskSchema>

    @Query("SELECT * FROM TaskSchema WHERE  priority = :priority")
    suspend fun filterPriority(priority: Int): List<TaskSchema>

    @Query("SELECT * FROM TaskSchema WHERE dueDate = :date")
    suspend fun filterDate(date: String): List<TaskSchema>

    @Query("SELECT * FROM TaskSchema WHERE dueDate BETWEEN :start AND :end")
    suspend fun filterDate(start: String, end: String): List<TaskSchema>

    @Query("SELECT * FROM TaskSchema WHERE status = :status AND priority = :priority")
    suspend fun filter(status: Int, priority: Int): List<TaskSchema>

    @Query("SELECT * FROM TaskSchema WHERE status = :status AND priority = :priority AND dueDate = :date")
    suspend fun filter(status: Int, priority: Int, date: String): List<TaskSchema>

    @Query("SELECT * FROM TaskSchema WHERE status = :status AND priority = :priority AND dueTimestamp BETWEEN :startDate AND :endDate")
    suspend fun filter(status: Int, priority: Int, startDate: String, endDate: String): List<TaskSchema>
}

/**
 * @param priority  1= low, 2=medium, 3=high
 * @param status  1= Todo, 2=inProgress, 3=done
 * @param dueDate only store the date so that can use as query
 */
@Entity
data class TaskSchema(
    @PrimaryKey
    val createdOn: Long,
    val title: String,
    val description: String?,
    val dueTimestamp: Long?,
    val priority:Int,
    val status: Int,
    val dueDate: String?=dueTimestamp?.toDateString()
)
