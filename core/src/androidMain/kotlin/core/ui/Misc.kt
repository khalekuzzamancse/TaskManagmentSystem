package core.ui
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
@Composable
fun containerBorderColor()= MaterialTheme.colorScheme.primary
@Composable
fun textFieldColor()=TextFieldDefaults.colors().copy(
    focusedContainerColor = Color.Transparent,
    unfocusedContainerColor = Color.Transparent,
    disabledContainerColor = Color.Transparent,
    focusedIndicatorColor = Color.Transparent,
    unfocusedIndicatorColor = Color.Transparent,
    disabledIndicatorColor = Color.Transparent
)
@Composable
fun  Modifier.textFieldBorder()=this. border(width = (0.5).dp,
    color = MaterialTheme.colorScheme.onSurface, shape = RoundedCornerShape(8.dp))