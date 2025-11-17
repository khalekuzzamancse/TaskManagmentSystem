package core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import core.logic.FeedbackController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenStrategy(
    topBar: @Composable () -> Unit={},
    controller: FeedbackController? = null,
    fab: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    content: @Composable (Modifier) -> Unit
) {
    val isLoading = (controller?.isLoading?.collectAsState()?.value) ?: false
    val message = controller?.messageToUi?.collectAsState()?.value


    val hostState = remember { SnackbarHostState() }

    // reactively show snackbar when message changes
    LaunchedEffect(message) {
        if (message != null) {
            hostState.showSnackbar(message)
            controller.clearMessage() // dismissable after shown
        }
    }

    Scaffold(
        topBar =topBar,
        floatingActionButton = fab,
        bottomBar = {
            Box {
                bottomBar()
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Gray.copy(alpha = 0.95f))
                    )
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState)
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.TopCenter
        ) {
            content(Modifier)
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Gray.copy(alpha = 0.95f)),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(64.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScreenStrategy(
    modifier: Modifier = Modifier,
    title: ( @Composable () -> Unit)? = null,
    navigationIcon: ( @Composable () -> Unit)? = null,
    controller: FeedbackController? = null,
    fab: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit,
    navRail: (@Composable () -> Unit)? = null, // new optional param
    content: @Composable BoxScope.(Modifier) -> Unit
) {
    val isLoading = controller?.isLoading?.collectAsState()?.value ?: false
    val hasTopBar = title != null || navigationIcon != null

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val isWideScreen = maxWidth > 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                if (hasTopBar) {
                    TopAppBar(
                        title = { title?.invoke() },
                        navigationIcon = { navigationIcon?.invoke() },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
                        )
                    )
                }
            },
            bottomBar = {
                if (!isWideScreen) {
                    Box {
                        bottomBar()
                        if (isLoading) {
                            Box(
                                modifier = Modifier
                                    .matchParentSize()
                                    .background(Color.Gray.copy(alpha = 0.95f))
                            )
                        }
                    }
                }
            },
            floatingActionButton = { fab() }
        ) { innerPadding ->
            Row(modifier = Modifier
                .padding(innerPadding)
                .fillMaxHeight()
            ) {
                if (isWideScreen) {

                    navRail?.invoke() // show navRail on wide screens
                    SpacerHorizontal(4)
                }
                Content(modifier =Modifier
                    .fillMaxSize()
                    .weight(1f),
                    content=content,
                    isLoading=isLoading)
            }
        }
    }
}
@Composable
fun RowScope.Content(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.(Modifier) -> Unit,
    isLoading: Boolean
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.TopCenter
    ) {
        content(Modifier)
        if (isLoading) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(Color.Gray.copy(alpha = 0.95f)),
                contentAlignment = Alignment.Center
            ) {
                LoadingView(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

