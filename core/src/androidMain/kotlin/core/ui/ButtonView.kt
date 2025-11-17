package core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import core.language.VoidCallback

@Composable
fun IconButtonView(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    tint: Color=Color.Unspecified,
    contentDescription: String="an action",
    onClick: VoidCallback
){
    Icon(
        imageVector = icon,
        contentDescription = contentDescription,
        tint = tint,
        modifier = modifier
            .size(48.dp)
            .padding(8.dp)
            .clickable{
            onClick()
        }
    )

}
@Composable
fun ButtonView(
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface (
        shape = RoundedCornerShape(8.dp),
        modifier = modifier.clickable{
            onClick()
        }
    ){
        Row(
            modifier = Modifier
                .height(48.dp)
                .background(MaterialTheme.colorScheme.primary)
                .padding(vertical = 8.dp, horizontal = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(icon, contentDescription = label,tint = MaterialTheme.colorScheme.onPrimary)
            Spacer(Modifier.width(4.dp))
            Text(text = label, color = MaterialTheme.colorScheme.onPrimary)
        }

    }

}
@Composable
fun ButtonView(
    modifier: Modifier = Modifier,
    label: String,
    disable: Boolean=false,
    onClick: () -> Unit
) {
    val backgroundColor= if (disable) Color.Gray else MaterialTheme.colorScheme.primary
    Surface (
        shape = RoundedCornerShape(8.dp),
        modifier = modifier
            .then(
                if (disable) Modifier else Modifier.clickable{ onClick()}
            )
    ){
        Row(
            modifier = Modifier
                .height(48.dp)
                .background(backgroundColor)
                .padding(vertical = 8.dp, horizontal = 16.dp)
            ,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = label, color =if (backgroundColor.luminance()>0.5f ) Color.Black else Color.White)
        }

    }

}
@Composable
fun BackIcon(modifier: Modifier = Modifier,onClick: () -> Unit) {
    IconButton(onClick = onClick,modifier = modifier) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = "back"
        )
    }

}
@Composable
fun CloseIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    color: Color = MaterialTheme.colorScheme.primaryContainer,
    iconColor:Color= MaterialTheme.colorScheme.primary,
    size: Dp = 20.dp,
    borderThickness: Dp = 0.1.dp,
    borderColor: Color = color.contentColor(),
    elevation: Dp = 2.dp
) {
    Surface(
        modifier = modifier
            .size(size)
            .background(
                color = color,
                shape = CircleShape
            )
            .border(
                border = BorderStroke(
                    width = borderThickness,
                    color = borderColor,
                ),
                shape = CircleShape
            )
        ,
        shadowElevation = elevation,
        shape = CircleShape
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    onClick()
                }
            ,
            contentAlignment = Alignment.Center,
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "filter",
                tint = iconColor
            )
        }

    }

}