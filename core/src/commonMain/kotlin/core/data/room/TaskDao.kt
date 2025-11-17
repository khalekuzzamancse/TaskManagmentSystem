package core.data.room
import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(item: TaskSchema)
    @Query("SELECT * FROM TaskSchema WHERE createdOn = :id LIMIT 1")
    suspend fun readTask(id: Long): TaskSchema?
    @Query("DELETE FROM TaskSchema WHERE createdOn = :id")
    suspend fun deleteTaskById(id: Long)
    @Query("SELECT * FROM TaskSchema")
     fun observerTasks(): Flow<List<TaskSchema>>
}

/**
 * @param priority  1= low, 2=medium, 3=high
 * @param status  1= Todo, 2=inProgress, 3=done
 */
@Entity
data class TaskSchema(
    @PrimaryKey
    val createdOn: Long,
    val title: String,
    val description: String?,
    val dueDate: Long?,
    val priority:Int,
    val status: Int,
)
