@file:Suppress("ComposableNaming", "Unused")

package features.presentation._navigation

import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.ui.SpacerHorizontal
import core.ui.SpacerVertical


enum class BottomBarItem {
    Home, UserManual, Create, AboutUs, AboutApp
}

@Composable
fun BottomBar(
    modifier: Modifier = Modifier,
    selectedRoute: BottomBarItem,
    onHomeClick: () -> Unit,
    onCreateRequest: () -> Unit,
    onAboutUsRequest: () -> Unit,
) {

    Surface(
        modifier = modifier,
        shadowElevation = 16.dp,
        tonalElevation = 16.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
            ,
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            BarItem(
                modifier=Modifier,
                outlinedIcon = Icons.Outlined.Home,
                onClick = onHomeClick,
                selected = selectedRoute == BottomBarItem.Home,
                label = "Home",
                onPositioned = {}
            )
            BarItem(
                modifier=Modifier,
                outlinedIcon = Icons.Outlined.Person,
                onClick = onAboutUsRequest,
                selected = selectedRoute == BottomBarItem.AboutUs,
                label = "About Us",
                onPositioned = {

                }
            )
        }

    }
}


@Composable
fun NavRail(
    modifier: Modifier = Modifier,
    selectedRoute: BottomBarItem,
    onHomeClick: () -> Unit,
    onCreateRequest: () -> Unit,
    onAboutUsRequest: () -> Unit,
) {
    Surface(
        shadowElevation = 16.dp,
        tonalElevation = 16.dp
    ) {
        Column(
            modifier = modifier
                .width(IntrinsicSize.Max)
                .fillMaxHeight()
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,
        ) {
            NavRailItem(
                modifier=Modifier.fillMaxWidth(),
                icon = Icons.Outlined.Home,
                onClick = onHomeClick,
                selected = selectedRoute == BottomBarItem.Home,
                label = "Home",
                onPositioned = {}
            )
            SpacerVertical(32)
            NavRailItem(
                modifier=Modifier.fillMaxWidth(),
                icon = Icons.Outlined.Person,
                onClick = onAboutUsRequest,
                selected = selectedRoute == BottomBarItem.AboutUs,
                label = "About Us",
                onPositioned = {

                }
            )
        }
    }

}
@Composable
fun BarItem(
    modifier: Modifier = Modifier,
    label: String,
    outlinedIcon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    onPositioned: (IntOffset) -> Unit = {}
) {
    val shape = RoundedCornerShape(12.dp)
    val backgroundColor = if (selected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    else
        Color.Transparent

    // The modern Material 3 approach:
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        shape = shape,
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = modifier.width(IntrinsicSize.Max).padding(horizontal = 4.dp)
        ) {
            _Icon(
                label = label,
                outlinedIcon = outlinedIcon,
                selected = selected,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = backgroundColor, shape = RoundedCornerShape(12.dp))
                    .padding(vertical = 8.dp)
            )
            SpacerVertical(4)
            _Label(label = label, selected = selected)
        }

    }
}

@Composable
fun NavRailItem(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit,
    onPositioned: (IntOffset) -> Unit = {}
) {
    val shape = RoundedCornerShape(12.dp)
    val backgroundColor = if (selected)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
    else
        Color.Transparent

    // The modern Material 3 approach:
    Surface(
        modifier = modifier
            .clip(shape)
            .clickable(
                onClick = onClick,
                interactionSource = remember { MutableInteractionSource() },
                indication = LocalIndication.current
            ),
        color = backgroundColor,
        shape = shape,
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = modifier.padding(8.dp),

            ) {
            _Icon(
                label = label,
                outlinedIcon = icon,
                selected = selected,
                modifier = Modifier
            )
            SpacerHorizontal(4)
            _Label(label = label, selected = selected)
        }

    }
}


@Composable
private fun _Icon(
    modifier: Modifier = Modifier,
    label: String,
    outlinedIcon: ImageVector,
    selected: Boolean,
) {
    Icon(
        modifier = modifier,
        imageVector =  outlinedIcon,
        contentDescription = label,
        tint = if (selected) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
    )
}

@Composable
private fun _Label(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean
) {
    Text(
        text = label,
        color = if (selected) MaterialTheme.colorScheme.primary else Color.Unspecified,
        fontWeight = if (selected) FontWeight.W500 else FontWeight.W400,
        maxLines = 1,
        fontSize = 13.sp,
        modifier = Modifier
    )
}