package core.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance

typealias ComposableWithModifier= @Composable (Modifier)->Unit
fun Color.contentColor()=if(this.luminance()<0.5f) Color.White else Color.Black