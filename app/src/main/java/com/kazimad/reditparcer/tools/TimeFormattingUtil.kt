package com.kazimad.reditparcer.tools

import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.text.DateFormat
import java.util.*

class TimeFormattingUtil {

    companion object {
        const val SERVER_RESPONSE_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'"
        const val SERVER_TIME_PATTERN = "HH:mm:ss"
        const val DISPLAY_DATE_PATTERN_13 = "dd.MM"
        const val DISPLAY_TIME_DATE_PATTERN_1 = "HH:mm - MM/dd/yyyy"

        fun formatDateWithPattern(millis: Long, pattern: String): String {
            return DateTimeFormat.forPattern(pattern).withLocale(Locale.getDefault())
                    .print(millis)
        }

        fun formatDateWithLocale(millis: Long, locale: Locale): String {
            return DateFormat.getDateInstance(DateFormat.DEFAULT, Locale.getDefault())
                    .format(Date(millis))
        }

        fun formatDate(millis: Long, pattern: String): String {
            return DateTimeFormat.forPattern(pattern).print(DateTime(millis))
        }
    }
}
