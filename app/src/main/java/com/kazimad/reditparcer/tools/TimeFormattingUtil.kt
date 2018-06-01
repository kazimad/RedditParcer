package com.kazimad.reditparcer.tools

import org.joda.time.format.DateTimeFormat
import java.util.*

class TimeFormattingUtil {
    companion object {
        const val DISPLAY_TIME_DATE_PATTERN_1 = "HH:mm:ss-MM.dd.yyyy"
        fun formatDateWithPattern(millis: Long, pattern: String): String {
            return DateTimeFormat.forPattern(pattern).withLocale(Locale.getDefault())
                    .print(millis)
        }
    }
}
