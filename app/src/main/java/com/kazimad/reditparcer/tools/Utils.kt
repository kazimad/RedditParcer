package com.kazimad.reditparcer.tools

import android.content.res.Resources
import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.R
import java.util.*


/**
 * Created by kazimad on 15.02.2018.
 */
class Utils {
    companion object {

        fun getResString(resId: Int, vararg formatArgs: Any): String {
            try {
                return App.instance.getString(resId, *formatArgs)
            } catch (e: MissingFormatArgumentException) {
                e.printStackTrace()
            }
            return ""
        }

        @JvmStatic
        fun getResString(resId: Int): String {
            try {
                return App.instance.getString(resId)
            } catch (ex: Resources.NotFoundException) {
                ex.printStackTrace()
            }
            return ""
        }

        fun getFriendlyTime(timestamp: Long): String {
            val dateTime = Date(timestamp * 1000)
            val sb = StringBuffer()
            val current = Calendar.getInstance().time
            var diffInSeconds = ((current.time - dateTime.time) / 1000).toInt()

            val sec = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
            diffInSeconds /= 60
            val min = if (diffInSeconds >= 60) (diffInSeconds % 60) else diffInSeconds
            diffInSeconds /= 60
            val hrs = if (diffInSeconds >= 24) (diffInSeconds % 24) else diffInSeconds
            diffInSeconds /= 24
            val days = if (diffInSeconds >= 30) (diffInSeconds % 30) else diffInSeconds
            diffInSeconds /= 30
            val months = if (diffInSeconds >= 12) (diffInSeconds % 12) else diffInSeconds
            diffInSeconds /= 12
            val years = diffInSeconds

            when {
                years > 0 -> {
                    yearsCounter(sb, years, months)
                }
                months > 0 -> {
                    monthCounter(sb, years, days)
                }
                days > 0 -> {
                    daysCounter(sb, days, hrs)
                }
                hrs > 0 -> {
                    hourCounter(sb, hrs, min)
                }
                min > 0 -> {
                    minutesCounter(sb, min, sec)
                }
                else -> {
                    lesMinuteCounter(sb, sec)
                }
            }
            sb.append(Utils.getResString(R.string.time_ago))
            return sb.toString()
        }


        private fun yearsCounter(sb: StringBuffer, years: Int, months: Int) {
            if (years == 1) {
                sb.append(Utils.getResString(R.string.time_year))
            } else {
                sb.append(Utils.getResString(R.string.time_years, years))
            }
            if (years <= 6 && months > 0) {
                if (months == 1) {
                    sb.append(Utils.getResString(R.string.time_and_a_month))
                } else {
                    sb.append(Utils.getResString(R.string.time_and_months, months))
                }
            }
        }

        private fun monthCounter(sb: StringBuffer, months: Int, days: Int) {
            if (months == 1) {
                sb.append(Utils.getResString(R.string.time_month))
            } else {
                sb.append(Utils.getResString(R.string.time_months, months))
            }
            if (months <= 6 && days > 0) {
                if (days == 1) {
                    sb.append(Utils.getResString(R.string.time_and_a_day))
                } else {
                    sb.append(Utils.getResString(R.string.time_and_days, days))
                }
            }
        }

        private fun daysCounter(sb: StringBuffer, days: Int, hrs: Int) {
            if (days == 1) {
                sb.append(Utils.getResString(R.string.time_day))
            } else {
                sb.append(Utils.getResString(R.string.time_days, days))
            }
            if (days <= 3 && hrs > 0) {
                if (hrs == 1) {
                    sb.append(Utils.getResString(R.string.time_and_an_hour))
                } else {
                    sb.append(Utils.getResString(R.string.time_and_hours, hrs))
                }
            }
        }

        private fun hourCounter(sb: StringBuffer, hrs: Int, min: Int) {
            if (hrs == 1) {
                sb.append(Utils.getResString(R.string.time_hour))
            } else {
                sb.append(Utils.getResString(R.string.time_hours, hrs))
            }
            if (min > 1) {
                sb.append(Utils.getResString(R.string.time_and_minutes, min))
            }
        }

        private fun minutesCounter(sb: StringBuffer, min: Int, sec: Int) {
            if (min == 1) {
                sb.append(Utils.getResString(R.string.time_minute))
            } else {
                sb.append(Utils.getResString(R.string.time_minutes, min))
            }
            if (sec > 1) {
                sb.append(Utils.getResString(R.string.time_and_seconds, sec))
            }
        }

        private fun lesMinuteCounter(sb: StringBuffer, sec: Int) {
            if (sec <= 1) {
                sb.append(Utils.getResString(R.string.time_second))
            } else {
                sb.append(Utils.getResString(R.string.time_about_seconds, sec))
            }
        }
    }
}