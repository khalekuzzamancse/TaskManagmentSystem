package features.domain

data class TaskModel(
    val title: String,
    val description: String?,
    val dueDate: Long?,
    val priority: PriorityModel,
    val status: StatusModel,
    val createdOn: Long,
    val id: String
)