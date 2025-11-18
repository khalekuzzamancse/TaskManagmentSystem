@file:Suppress("ComposableNaming")

package features.presentation._navigation

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import core.ui.ButtonView
import features.presentation.misc.AboutUsScreen
import features.presentation.task_creation.TaskCreationScreen
import features.presentation.task_updation.TaskUpdateScreen
import features.presentation.tasklist.TaskListScreen

@Composable
fun NavigationRoute() {
    _NavigationRoot()
}

@Composable
fun _NavigationRoot(
    modifier: Modifier = Modifier
) {

    val viewModel = viewModel { NavigationViewModel() }
    val selected = viewModel.selected.collectAsState().value
    var backPressCountOnHome = remember { 0 }
    val context = LocalContext.current
    val backStack = viewModel.backStack
    BackHandler {
        if (backStack.size == 1) {
            backPressCountOnHome++
        } else {
            backPressCountOnHome = 0//reset
        }
        if (backPressCountOnHome >= 2) {
            (context as? Activity)?.finish()
        }
        viewModel.onBack()


    }

    val navRail: @Composable () -> Unit = remember(selected) {
        {
            NavRail(
                modifier = Modifier,
                selectedRoute = selected,
                onHomeClick = {
                    viewModel.onSelect(Route.Home.route)
                },
                onCreateRequest = {

                },
                onAboutUsRequest = {
                    viewModel.onSelect(Route.AboutUs.route)
                },
            )
        }
    }
    val bottomBar: @Composable () -> Unit = remember(selected) {
        {
            BottomBar(
                selectedRoute = selected,
                onHomeClick = {
                    viewModel.onSelect(Route.Home.route)
                },
                onCreateRequest = {

                },
                onAboutUsRequest = {
                    viewModel.goToAboutUs()
                },

                )
        }
    }
    val fab: @Composable () -> Unit = remember {
        @Composable {
            ButtonView(
                modifier = Modifier,
                label = "Create Task",
                icon = Icons.Default.Add
            ) {
                viewModel.onSelect(Route.CreateTask.route)
            }

        }
    }
    NavDisplay(
        modifier = modifier,
        backStack = backStack,
        onBack = {
            viewModel.onBack()
        },
        entryDecorators = listOf(
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
            rememberSceneSetupNavEntryDecorator()
        ),
        entryProvider = { key ->
            when (key) {
                is Route.Home -> {
                    NavEntry(key) {
                        TaskListScreen(
                            bottomBar = bottomBar,
                            navRail = navRail,
                            fab = fab,
                            onDetailsRequest = {
                                viewModel.goToUpdate(it)
                            }
                        )
                    }
                }

                is Route.Details -> {
                    NavEntry(key) {
                        TaskUpdateScreen(id = key.id, onBack = viewModel::onBack)
                    }
                }

                is Route.CreateTask -> {
                    NavEntry(key) {
                        TaskCreationScreen(
                            onBack = viewModel::onBack
                        )
                    }
                }

                Route.AboutUs -> {
                    NavEntry(key) {
                        AboutUsScreen(
                            fab = {},
                            bottomBar = bottomBar,
                            navRail = navRail
                        )
                    }
                }

                else -> throw kotlin.RuntimeException("Invalid root")
            }
        }
    )

}

