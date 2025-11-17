package core.logic

import core.language.CustomException
import core.language.Logger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

interface FeedbackController {
    val isLoading: StateFlow<Boolean>
    val messageToUi: StateFlow<String?>
    fun updateMessage(message: String)
    fun onException(exception: Throwable)
    fun clearMessage()
    fun startLoading()
    fun stopLoading()
}

class FeedbackControllerImpl : FeedbackController {
    private val tag="ScreenFeedbackControllerImpl"
    private val scope = CoroutineScope(Dispatchers.Default)
    override val isLoading = MutableStateFlow(false)
    override val messageToUi = MutableStateFlow<String?>(null)
    override fun updateMessage(message: String) {
        if (messageToUi.value == message) return
        scope.launch {
            messageToUi.value = message
            delay(4_000)
            clearMessage()
        }
    }

    override fun onException(exception: Throwable) {
        Logger.on(tag,"$exception")
       if(exception is CustomException){
           updateMessage(exception.message)
       }
        else{
            val exceptionMessage = exception.message ?: "Something went wrong"
            updateMessage(exceptionMessage)
        }
    }

    override fun clearMessage() {
        messageToUi.value = null
    }

    override fun startLoading() {
        isLoading.value = true
    }

    override fun stopLoading() {
        isLoading.value = false
    }

}

