package com.kazimad.reditparcer.tools

import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import com.kazimad.reditparcer.App
import java.sql.Timestamp
import java.util.*


/**
 * Created by kazimad on 15.02.2018.
 */
class Utils {
    companion object {
        fun dipToPx(context: Context, dp: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.getResources().getDisplayMetrics()).toInt()
        }

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

        fun getScreenWidth(): Int {
            val displayMetrics = DisplayMetrics()
            val windowManager = App.instance.applicationContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager.defaultDisplay.getMetrics(displayMetrics)
            return displayMetrics.widthPixels
        }

        fun generateRandomString(): String {
            val SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890"
            val salt = StringBuilder()
            val rnd = Random()
            while (salt.length < 10) { // length of the random string.
                val index = (rnd.nextFloat() * SALTCHARS.length).toInt()
                salt.append(SALTCHARS[index])
            }
            return salt.toString()

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

            if (years > 0) {
                if (years == 1) {
                    sb.append("a year")
                } else {
                    sb.append("$years years")
                }
                if (years <= 6 && months > 0) {
                    if (months == 1) {
                        sb.append(" and a month")
                    } else {
                        sb.append(" and $months months")
                    }
                }
            } else if (months > 0) {
                if (months == 1) {
                    sb.append("a month")
                } else {
                    sb.append("$months months")
                }
                if (months <= 6 && days > 0) {
                    if (days == 1) {
                        sb.append(" and a day")
                    } else {
                        sb.append(" and $days days")
                    }
                }
            } else if (days > 0) {
                if (days == 1) {
                    sb.append("a day")
                } else {
                    sb.append("$days days")
                }
                if (days <= 3 && hrs > 0) {
                    if (hrs == 1) {
                        sb.append(" and an hour")
                    } else {
                        sb.append(" and $hrs hours")
                    }
                }
            } else if (hrs > 0) {
                if (hrs == 1) {
                    sb.append("an hour")
                } else {
                    sb.append("$hrs hours")
                }
                if (min > 1) {
                    sb.append(" and $min minutes")
                }
            } else if (min > 0) {
                if (min == 1) {
                    sb.append("a minute")
                } else {
                    sb.append("$min minutes")
                }
                if (sec > 1) {
                    sb.append(" and $sec seconds")
                }
            } else {
                if (sec <= 1) {
                    sb.append("about a second")
                } else {
                    sb.append("about $sec seconds")
                }
            }

            sb.append(" ago")

            return sb.toString()
        }
    }
}