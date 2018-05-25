package com.kazimad.reditparcer.tools

import android.util.Log
import com.kazimad.reditparcer.BuildConfig
import java.io.CharArrayWriter
import java.io.PrintWriter


class Logger {
    companion object {
        private val UNKNOWN_EXCEPTION = "Unknown exception"
        private const val MY_LOG = "myLog"
        private val LOGS_ENABLED = BuildConfig.DEBUG

        fun log(message: String) {
            if (LOGS_ENABLED) {
                Log.d(MY_LOG, message)
            }
        }

        fun log(tag: String, message: String) {
            if (LOGS_ENABLED) {
                Log.d(tag, message)
            }
        }

        fun log(throwable: Throwable) {
            if (LOGS_ENABLED) {
                Log.d(MY_LOG, getStackTrace(throwable))
            }
        }

        private fun getStackTrace(throwable: Throwable): String {
            try {
                val cw = CharArrayWriter()
                val w = PrintWriter(cw)
                throwable.printStackTrace(w)
                w.close()
                return cw.toString()
            } catch (e: Exception) {
                return UNKNOWN_EXCEPTION
            }

        }
    }
}