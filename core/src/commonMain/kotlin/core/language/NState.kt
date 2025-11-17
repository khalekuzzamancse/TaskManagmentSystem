package core.language

/**
 * - [True]   Equivalent to `true`
 * - [NotDecided] Equivalent to `null`
 * - [False] Equivalent to `false`
 */
sealed class NState(open val value: String) {
    data object True : NState("True")
    data object NotDecided: NState("Not Decided")
    data object False : NState("False")
    data class InProgress(override val value: String = "In Progress") : NState(value)
    data class Error(override val value: String = "Error") : NState(value)
    fun isInProgress() = this is InProgress
    fun isNotInProgress() = this !is InProgress
    fun isTrue() = this is True
    fun isNotTrue() = this !is True
    fun isFalse() = this is False
    fun isNotFalse() = this !is False
    fun isError() = this is Error
    fun isNotError() = this !is Error
    fun isNotDecided() = this is NotDecided
    fun isDecided() = this !is NotDecided

}