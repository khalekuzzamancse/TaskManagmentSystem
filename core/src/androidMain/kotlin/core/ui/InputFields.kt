package core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextFieldView(
    modifier: Modifier = Modifier,
    value: String,
    readOnly: Boolean = false,
    singleLine: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    placeholder: String? = null
) {
    OutlinedTextField(
        modifier= modifier.fillMaxWidth().textFieldBorder(),
        value = value,
        colors = textFieldColor(),
        onValueChange = onValueChange,
        readOnly = readOnly,
        trailingIcon = trailingIcon,
        singleLine = singleLine,
        placeholder = {
            if (placeholder != null) {
                Text(text = placeholder, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
            }
        }
    )

}

@Composable
 fun CustomTextField(
    modifier: Modifier,
    hints: String,
    value: String,
    singleLine: Boolean = true,
    onValueChanged: (String) -> Unit,
) {

    val borderColor = MaterialTheme.colorScheme.tertiary
    val placeholderColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
    val textColor = Color.Unspecified
    val fontSize = 15.sp
    val keyboardController = LocalSoftwareKeyboardController.current
    BasicTextField(
        enabled = true,
        value = value,
        onValueChange = onValueChanged,
        textStyle = TextStyle(fontSize = fontSize, color = textColor),
        singleLine = singleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide()
            }
        ),
        decorationBox = { innerText ->
            Row(
                modifier
                    .border(
                        width = 2.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(vertical = 10.dp, horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    if (value.isEmpty()) {
                        _Placeholder(hints, fontSize, placeholderColor)
                    }
                    // Call innerText in both cases to ensure the cursor is shown (if enabled)
                    innerText()
                }
            }
        }
    )
}

@Composable
fun _Placeholder(text: String, fontSize: TextUnit, placeholderColor: Color) {
    Text(
        text = text,
        fontSize = fontSize,
        color = placeholderColor,

        )
}


@Composable
fun DescriptionField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        colors = textFieldColor(),
        modifier = modifier.textFieldBorder()
    )
}
