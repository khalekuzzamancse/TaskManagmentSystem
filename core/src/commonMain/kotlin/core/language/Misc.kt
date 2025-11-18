package core.language

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

typealias VoidCallback = () -> Unit
@OptIn(ExperimentalTime::class)
fun Long.toDateString(): String {
    val instant = Instant.fromEpochMilliseconds(this)
    val localDate = instant.toLocalDateTime(TimeZone.currentSystemDefault()).date
    return localDate.toString()  // Returns in "yyyy-MM-dd" format
}