package core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    modifier: Modifier = Modifier,
    onDateSelected: (Long?) -> Unit,
) {

    val datePickerState = rememberDatePickerState()
    var selected by rememberSaveable { mutableStateOf<Long?>(null) }
    val snapshot= selected
    var showDialog by rememberSaveable { mutableStateOf(false) }
    TextFieldView (
        modifier = modifier.fillMaxWidth(),
        value = if (snapshot == null) "" else DateTimeUtils.formatDateInMs(snapshot),
        onValueChange = {},
        placeholder =  if (snapshot == null) "Select date" else null,
        readOnly = true,
        trailingIcon = {
            if (selected==null){
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Pick date",
                    tint = MaterialTheme.colorScheme.primary,
                  modifier = Modifier.clickable { showDialog = true }
                )
            }
            else{
                IconButtonView(
                    icon = Icons.Default.Cancel,
                    tint = Color.Red
                ) {
                    selected=null
                }
            }

        },

        )
    if (showDialog) {
        DatePickerDialog(
            onDismissRequest = {
                onDateSelected(null)
                showDialog = false
            },
            confirmButton = {
                TextButton(onClick = {
                    selected = datePickerState.selectedDateMillis
                    onDateSelected(selected)
                    showDialog = false
                }) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    onDateSelected(null)
                    showDialog = false
                }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = datePickerState,)
        }
    }

}
