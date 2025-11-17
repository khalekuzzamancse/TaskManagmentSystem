@file:Suppress("FunctionName")
package core.ui

import java.lang.System.currentTimeMillis
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

/** - TODO: Move to Kotlin for KMP Support*/
data class DateTime(
    val date: Long,
    val hour: Int,
    val minute: Int,
    val second: Int = 0
) {

    /** - dd-mm-yy or dd/mm/yy */
    fun formatDate(separator: String = "-"): String {
        val pattern = "dd${separator}MM${separator}yy"
        val formatter = DateTimeFormatter.ofPattern(pattern)
        val date = Instant.ofEpochMilli(date)
            .atZone(ZoneOffset.UTC)
            .toLocalDate()
        return date.format(formatter)
    }

    /** - 14:05  or 02:05:07 PM*/
    fun formatTime(use24Hour: Boolean = true, includeSecond: Boolean = false): String {
        return if (use24Hour) {
            if (includeSecond)
                "%02d:%02d:%02d".format(hour, minute, second)
            else
                "%02d:%02d".format(hour, minute)
        } else {
            val amPm = if (hour < 12) "AM" else "PM"
            val hour12 = when {
                hour == 0 -> 12
                hour > 12 -> hour - 12
                else -> hour
            }
            if (includeSecond)
                "%02d:%02d:%02d %s".format(hour12, minute, second, amPm)
            else
                "%02d:%02d %s".format(hour12, minute, amPm)
        }
    }

    /** - Mon 12 July 2025 or Mon 12 July */
    fun formatPrettyDate(includeYear: Boolean = false): String {
        val pattern = if (includeYear) "EEE dd MMM yyyy" else "EEE dd MMM"//MMM=3 Char, MMMM=all character of month
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
        val date = Instant.ofEpochMilli(date)
            .atZone(ZoneId.of("UTC"))
            .toLocalDate()
        return date.format(formatter)
    }

    fun toEncodedIso8601(): String {
        val dateTime = Instant.ofEpochMilli(date)
            .atZone(ZoneOffset.UTC)
            .withHour(hour)
            .withMinute(minute)
            .withSecond(second)
            .toLocalDateTime()

        val raw = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"))
        return java.net.URLEncoder.encode(raw, "UTF-8")
    }
}

/**
 * No Java Dependencies, can use with KMP
 */
object DateTimeUtils {
    /** as 15 NOV 2025 */
    fun formatDateInMs(timestamp: Long): String {
        val sdf = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
        val date = Date(timestamp)
        return sdf.format(date)
    }
    /**
     * Converts a given timestamp (in milliseconds) into a pair of formatted date strings
     * representing the start and end of that specific day.
     *
     * The resulting strings follow the pattern:
     * - `from_date` → "yyyy-MM-ddT00:00:00.000"
     * - `to_date`   → "yyyy-MM-ddT23:59:00.000"
     *
     * The time portions (`T00:00:00.000` and `T23:59:00.000`) are fixed and represent
     * the beginning and end of the day respectively.
     *
     * @param timestamp the date/time value in milliseconds since the Unix epoch
     * @return a [Pair] of strings where:
     *  - `first` = start of the day (from_date)
     *  - `second` = end of the day (to_date)
     */
    fun formatDateRange(timestamp: Long): Pair<String, String> {
        val date = Date(timestamp)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        val datePart = sdf.format(date)
        val fromDate = "${datePart}T00:00:00.000"
        val toDate = "${datePart}T23:59:00.000"

        return fromDate to toDate
    }

    fun toTimeString(time: Pair<Int, Int>): String {
        val hour = if (time.first < 10) "0${time.first}" else "${time.first}"
        val minute = if (time.second < 10) "0${time.second}" else "${time.second}"
        return "$hour:$minute:00"
    }

    fun extractDateOrOriginal(date: String): String {
        return date.substringBefore("T")
//        return date.substringBefore("T").takeIf { it.matches(Regex("\\d{4}-\\d{2}-\\d{2}")) }
//            ?: "Unknown"
    }

    fun formatDate(dateInMillis: Long, separator: String = "-"): String {
        val totalDays =
            dateInMillis / (24 * 60 * 60 * 1000) // Convert millis to days since 1970-01-01

        // Epoch date
        var year = 1970
        var dayCount = totalDays.toInt()

        // Helper functions
        fun isLeapYear(y: Int): Boolean {
            return (y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)
        }

        // Days in year
        fun daysInYear(y: Int) = if (isLeapYear(y)) 366 else 365

        // Subtract years until dayCount < days in year
        while (true) {
            val daysThisYear = daysInYear(year)
            if (dayCount >= daysThisYear) {
                dayCount -= daysThisYear
                year++
            } else break
        }

        val monthsLengths =
            arrayOf(31, if (isLeapYear(year)) 29 else 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        var month = 0
        while (true) {
            if (dayCount >= monthsLengths[month]) {
                dayCount -= monthsLengths[month]
                month++
            } else break
        }

        val day = dayCount + 1 // dayCount is zero based, so add 1

        val dayStr = if (day < 10) "0$day" else "$day"
        val monthStr = if (month + 1 < 10) "0${month + 1}" else "${month + 1}"
        val yearStr = (year % 100).let { if (it < 10) "0$it" else "$it" }

        return "$dayStr$separator$monthStr$separator$yearStr"
    }

    fun withDayLabelOrOriginal(datetime: String): String {
        try {

            val input = java.time.LocalDateTime.parse(
                datetime,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            )
            val outputFormat = DateTimeFormatter.ofPattern("EEE d MMMM, HH:mm", Locale.ENGLISH)
            return input.format(outputFormat)
        } catch (_: Throwable) {
            return datetime
        }
    }

    /**
     * - 12 OCT 2025, 12:13 AM
     */
    fun withDayLabelOrOriginal1(datetime: String): String {
        return try {
            val input = java.time.LocalDateTime.parse(
                datetime,
                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            )
            val outputFormat = DateTimeFormatter.ofPattern("d MMM yyyy, hh:mm a", Locale.ENGLISH)
            input.format(outputFormat).uppercase(Locale.ENGLISH)
        } catch (_: Throwable) {
            datetime
        }
    }

    /**
     *
     */
    fun mm_dd_yy_amOrPm(datetime: String, separator: String = "/"): String {
        return try {
            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val input = LocalDateTime.parse(datetime, inputFormat)
            val outputPattern = "MM${separator}dd${separator}yy hh:mm a"
            val outputFormat = DateTimeFormatter.ofPattern(outputPattern, Locale.ENGLISH)

            input.format(outputFormat).lowercase()
        } catch (_: Throwable) {
            datetime
        }
    }
    fun dayMonthYearAMOrPM(datetime: String, separator: String = "/"): String {
        return try {
            val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS")
            val input = LocalDateTime.parse(datetime, inputFormat)
            val outputPattern = "dd${separator}MM${separator}yy hh:mm a"
            val outputFormat = DateTimeFormatter.ofPattern(outputPattern, Locale.ENGLISH)

            input.format(outputFormat).lowercase()
        } catch (_: Throwable) {
            datetime
        }
    }
    fun timeOnlyOrOriginal(datetime: String): String {
        try {
            val input = LocalDateTime.parse(datetime, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSS"))
            val outputFormat = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)
            return input.format(outputFormat).lowercase()
        }
        catch (_:Throwable){
            return  datetime
        }
    }
    fun getDateLabelOrOriginal(dateStr: String, separator: String = "-"): String {
        try {
            val inputDate = LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            val today = LocalDate.now()
            val yesterday = today.minusDays(1)

            return when (inputDate) {
                today -> "Today"
                yesterday -> "Yesterday"
                else -> inputDate.format(DateTimeFormatter.ofPattern("yyyy${separator}MM${separator}dd"))
            }
        }
        catch (_:Throwable){
            return dateStr
        }
    }
    fun getDateInMs(dayOffset: Int): Long {
        return Calendar.getInstance(TimeZone.getDefault()).apply {
            add(Calendar.DAY_OF_MONTH, dayOffset)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.timeInMillis
    }
    fun getDateToday(): Long {
        return currentTimeMillis()
    }

    /**
     * - 01 OCT 2025
     */
    fun formatDateAs_DD_MMM_YYYY(dateInMillis: Long): String {
        return try {
            val date = Date(dateInMillis)
            val formatter = SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH)
            formatter.format(date).uppercase(Locale.ENGLISH)
        } catch (_: Throwable) {
            ""
        }
    }


}

class Locker {
    private var locked: Boolean = false

    fun lock() {
        locked = true
    }

    fun unlock() {
        locked = false
    }

    fun isLocked(): Boolean = locked

    fun isNotLocked(): Boolean = !isLocked()
}

//fun toStringOrNA(value: Any?): String {
//    if (value == null)
//        return "N/A"
//    return "$value"
//}
fun Any?.toStringOrNA(): String {
    if (this == null)
        return "N/A"
    return "$this"
}

fun  String?.toDoubleOrZero(): Double {
    return try {
        this!!.toDouble()
    } catch (_: Throwable) {
        0.0
    }
}

fun String.keepDecimalPlaces(precision: Int): String {
    return toDoubleOrNull()?.let {
        "%.${precision}f".format(it)
    } ?: this
}
fun Double.upTo2decimalPoint(): String {
    return String.format(Locale.ENGLISH,"%.2f", this)
}
fun Float.upTo2decimalPoint(): String {
    return String.format(Locale.ENGLISH,"%.2f", this)
}