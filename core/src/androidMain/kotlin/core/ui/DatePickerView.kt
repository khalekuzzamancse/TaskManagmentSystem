package core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.outlined.CalendarToday
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerView(
    modifier: Modifier = Modifier,
    initial: Long? = null,
    onDateSelected: (Long?) -> Unit,
) {

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = initial
    )
    var selected by rememberSaveable(initial) { mutableStateOf(initial) }
    val snapshot = selected
    var showDialog by rememberSaveable { mutableStateOf(false) }
    TextFieldView(
        modifier = modifier.fillMaxWidth(),
        value = if (snapshot == null) "" else DateTimeUtils.formatDateInMs(snapshot),
        onValueChange = {},
        placeholder = if (snapshot == null) "Select date" else null,
        readOnly = true,
        trailingIcon = {
            if (selected == null) {
                Icon(
                    imageVector = Icons.Default.CalendarMonth,
                    contentDescription = "Pick date",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { showDialog = true }
                )
            } else {
                IconButtonView(
                    icon = Icons.Default.Cancel,
                    tint = Color.Red
                ) {
                    selected = null
                    onDateSelected(null)
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
            DatePicker(state = datePickerState)
        }
    }

}

@Preview
@Composable
private fun DateRangeFilterPreview() {
    val today = rememberSaveable { System.currentTimeMillis() }
    val sevenDaysAgo = rememberSaveable { today - TimeUnit.DAYS.toMillis(7) }
    var selectedRange by rememberSaveable() {
        mutableStateOf<Pair<Long?, Long?>>(sevenDaysAgo to today)
    }
    Column {
        TextHeading3(text = "Date Range")
        DateRangeFilter(
            modifier = Modifier,
            selectedRange = selectedRange,
            onSelection = {}
        )
    }


}

@Composable
fun DateRangeFilter(
    modifier: Modifier = Modifier,
    selectedRange: Pair<Long?, Long?>,
    onSelection: (Pair<Long?, Long?>) -> Unit
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    val start = selectedRange.first
    val end = selectedRange.second
    val selected = (start != null) || (end != null)

    if (showDialog) {
        DateRangePickerDialog(
            selected = selectedRange,
            onSelection = { range ->
                val start = range.first
                val end = range.second
                onSelection(start to end)

            },
            onDismiss = { showDialog = false }
        )
    }
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
            .textFieldBorder(),
        verticalAlignment = Alignment.CenterVertically

    ) {
        Row(
            modifier = modifier
                .clip(shape = RoundedCornerShape(8.dp))
                .weight(1f)
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val s =if (start != null) DateTimeUtils.formatDateAs_DD_MMM_YYYY(start) else null
            val e = if (end != null) DateTimeUtils.formatDateAs_DD_MMM_YYYY(end) else null
            val range=when{
                s != null && e != null -> "$s - $e"
                s != null -> s
                e != null -> e
                else -> null
            }
            if (range!=null) {
                Text(
                    text = range,
                    style = MaterialTheme.typography.bodyLarge,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select date range",
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }

            }

        }
        if (selected) {
            SpacerHorizontal(8)
            IconButtonView(
                icon = Icons.Default.Cancel,
                tint = Color.Red
            ) {
                onSelection(null to null)
            }
        } else {
            IconButtonView(
                icon = Icons.Outlined.CalendarToday,
                tint = MaterialTheme.colorScheme.primary
            ) {
                showDialog = true
            }

        }

    }


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangePickerDialog(
    selected: Pair<Long?, Long?>,
    onSelection: (Pair<Long?, Long?>) -> Unit,
    onDismiss: () -> Unit
) {
    val dateRangePickerState = rememberDateRangePickerState(
        initialSelectedStartDateMillis = selected.first,
        initialSelectedEndDateMillis = selected.second
    )

    DatePickerDialog(
        onDismissRequest = onDismiss,
        colors = DatePickerDefaults.colors(

        ),
        confirmButton = {

        },
        dismissButton = {

        }
    ) {
        DateRangePicker(
            state = dateRangePickerState,
            title = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    SpacerVertical(16)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,

                        ) {
                        IconButton(
                            onClick = onDismiss
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = "cancel",
                            )
                        }
                        Spacer(Modifier.weight(1f))
                        TextButton(
                            onClick = {
                                onSelection(
                                    Pair(
                                        dateRangePickerState.selectedStartDateMillis,
                                        dateRangePickerState.selectedEndDateMillis
                                    )
                                )
                                onDismiss()
                            }
                        ) {
                            Text("Save", color = Color.Black)
                        }

                    }
                    SpacerVertical(8)
                    Text(
                        text = "Select Range",
                        fontSize = 14.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                    SpacerVertical(8)

                }
            },
            showModeToggle = false,
            colors = DatePickerDefaults.colors(
                dayInSelectionRangeContainerColor = Color(0xFF05DBC6), //  background for range
                selectedDayContainerColor = Color(0xFF0478FF),
                selectedDayContentColor = Color.White,
                todayDateBorderColor = Color(0xFF275BB0)
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(500.dp)
        )
    }
}

@Composable
fun _ClearButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Row(
        modifier = modifier
            .clip(shape = RoundedCornerShape(16.dp))
            .clickable {
                onClick()
            }
            .height(48.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(32.dp)
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.ClearAll,
            contentDescription = "clear",
        )
        SpacerHorizontal(4)
        Text(
            text = "Clear",
        )
    }

}
