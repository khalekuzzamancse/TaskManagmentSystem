package features._core

import core.language.CustomException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

object GlobalMessenger{
    private val scope= CoroutineScope(Dispatchers.Default)
    private val _messageToUI= MutableStateFlow<String?>(null)
    val messageToUI= _messageToUI.asStateFlow()
    fun updateUiMessage(message:String){
        scope.launch {
            val newMessage= _messageToUI.value!=message
            if(newMessage){
                _messageToUI.update { message }
                delay(3000)
                _messageToUI.update { null } //Clear message
            }
        }
    }
    fun updateMessage(exception: CustomException){
        updateUiMessage(exception.message)
    }
}