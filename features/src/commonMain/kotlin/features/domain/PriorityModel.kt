@file:Suppress("unused")

package features.domain

enum class PriorityModel(val label: String) {
    LOW("Low"), MEDIUM("Medium"), HIGH("High");
    companion object{
        fun toPriorityOrThrow(label: String): PriorityModel = when (label) {
            LOW.label -> LOW
            MEDIUM.label -> MEDIUM
            HIGH.label -> HIGH
            else -> throw IllegalArgumentException("Invalid priority label: $label")
        }
    }

}
