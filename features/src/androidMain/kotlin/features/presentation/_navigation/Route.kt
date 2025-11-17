@file:Suppress("NewApi")

package features.presentation._navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey
import core.language.Logger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable

sealed interface NonTopRoute
sealed interface TopRoute
sealed interface Route {
    val route: String

    @Serializable
    data object Home : NavKey, Route, TopRoute {
        override val route = "Home"
    }

    @Serializable
    data object CreateTask : NavKey, Route, NonTopRoute {
        override val route = "CreateTask"
    }

    @Serializable
    data class Details (val id: String): NavKey, Route, TopRoute {
        override val route = "Details"
    }

}

class NavigationViewModel() : ViewModel() {
    val backStack: NavBackStack = mutableStateListOf(Route.Home)
    private val _selected = MutableStateFlow(BottomBarItem.Home)
    val selected = _selected.asStateFlow()

    /** Set the bitmap before navigation, passing bitmap via navigation is
    complex that is why doing this ...**/
    fun goToUpdate(id: String) {
        pushIfNotExist(Route.Details(id))
    }
    fun onSelect(destination: String) {
        Logger.off(tag = "Route", "onSelected->else:$destination")
        when {
            Route.Home.route == destination -> {
                pushIfNotExist(Route.Home)
                _selected.update { BottomBarItem.Home }
            }

            Route.CreateTask.route == destination -> {
                pushIfNotExist(Route.CreateTask)
                _selected.update { BottomBarItem.Create }
            }

            else -> {
                Logger.off(tag = "Route", "onSelected->else:$destination")
            }
        }

    }


    fun pop() {
        backStack.removeAt(backStack.lastIndex)
        val last = backStack.lastOrNull()
        val peek = last as? TopRoute
        updateSelection(peek)

    }
    private  fun  updateSelection(peek: TopRoute?){
        if (peek != null) {
            when (peek) {
                Route.Home -> _selected.update { BottomBarItem.Home }
                is Route.Details -> _selected.update { BottomBarItem.UserManual }
            }
        }

    }

    private fun pushIfNotExist(route: NavKey) {
        if (backStack.lastOrNull() != route) {
            backStack.add(route)
        }
    }

    fun onBack() {
        if (backStack.lastOrNull() is NonTopRoute) {
            backStack.removeAt(backStack.lastIndex)
            val last = backStack.lastOrNull()
            val peek = last as? TopRoute
            updateSelection(peek)
            return
        }
        if (Route.Home !in backStack) {
            backStack.clear()
            backStack.add(Route.Home)
        } else {
            // Pop everything until only Home remains
            while (backStack.size > 1) {
                backStack.removeAt(backStack.lastIndex)
            }
        }
        // Always update selected to Home
        _selected.update { BottomBarItem.Home }
    }


}
