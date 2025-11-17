@file:Suppress("UnUsed")
package core.language
open class CustomException(
    override val message: String,
    open val debugMessage: String,
    open val code: String = ""
) : Throwable(message) {
    override fun toString(): String {
        return "Code: $code\nMessage: $message\nDebug Message: $debugMessage"
    }
}

class TokenNotFoundException : CustomException(
    message = "Unable to retrieve Login session",
    debugMessage = "Failed to retrieve the token",
    code = "DE-TRE"
)

class UnauthorizedException : CustomException(
    message = "Login session expire",
    debugMessage = "May be the token is invalid or expired",
    code = "DE-Unt"
)

class JsonParsingException : CustomException(
    message = "Unexpected data format with code JPE",
    debugMessage = "Json Parsing error",
    code = "JPE"
)

class ServerConnectingException(
    private val exception: Throwable
) : CustomException(
    message = exception.toString(),
    debugMessage = "Server connection problem:\nMessage: ${exception}\nCause: ${exception::class.simpleName}",
    code = "SCE"
) {
    override fun toString(): String {
        return "ServerConnectingException -> ${super.toString()}"
    }
}

class UnKnownException(
    val exception: Any
) : CustomException(
    message = "Something went wrong",
    debugMessage = exception.toString(),
    code = "UNE"
) {
    override fun toString(): String {
        return "UnKnownException -> ${super.toString()}"
    }
}

class MessageFromServerException(
    val serverMessage: String
) : CustomException(
    message = serverMessage,
    debugMessage = "Server returned a message instead of expected response: $serverMessage",
    code = "MFSIOR"
) {
    override fun toString(): String {
        return "MessageFromServerException -> ${super.toString()}"
    }
}

class NetworkIOException(
    message: String,
    debugMessage: String
) : CustomException(
    message = message,
    debugMessage = debugMessage,
    code = "NIOE"
) {
    override fun toString(): String {
        return "NetworkIOException -> ${super.toString()}"
    }
}

class DuplicateRecordException : CustomException(
    message = "Exists already, Consider updating or deleting old one",
    debugMessage = "Attempt to insert an existing record for parser",
    code = "DE-DRE"
)

class RecordNotFoundException : CustomException(
    message = "No record found for the given criteria.",
    debugMessage = "Query returned no results for the provided primary key, document ID, or customer query.",
    code = "DE-RNFE"
)

class CreateFailException(
    message: String? = null,
    debugMessage: String = "Failed to create or configure the database instance"
) : CustomException(
    message = message ?: "Unable to create",
    debugMessage = debugMessage,
    code = "DE-DICNCE"
)

class NotImplementedException : CustomException(
    message = "Operation is not implemented yet",
    debugMessage = "No debug message",
    code = "NIY"
)

class UpdateFailException(
    message: String? = null,
    debugMessage: String = "Failed to create or configure the database instance"
) : CustomException(
    message = message ?: "Unable to write",
    debugMessage = debugMessage,
    code = "DE-DICNCE"
)

class ReadFailException(
    message: String? = null,
    debugMessage: String = "Failed to create or configure the database instance"
) : CustomException(
    message = message ?: "Unable to read",
    debugMessage = debugMessage,
    code = "DE-DICNCE"
)

fun toCustomException(exception: Any, fallbackDebugMsg: String): CustomException {
    return when (exception) {
        is CustomException -> exception
        else -> CustomException(
            message = "Something went wrong",
            debugMessage = fallbackDebugMsg
        )
    }
}