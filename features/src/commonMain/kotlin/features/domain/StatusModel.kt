@file:Suppress("unused")
package features.domain

enum class StatusModel(
    val label: String
) {
    TODO("Todo"), InProgress("In Progress"), DONE("Done");
    companion object{
        fun toStatusOrThrow(label:String):StatusModel= when(label){
            TODO.label -> TODO
            InProgress.label -> InProgress
            DONE.label -> DONE
            else -> throw IllegalArgumentException("Invalid status label: $label")
        }
    }

}
