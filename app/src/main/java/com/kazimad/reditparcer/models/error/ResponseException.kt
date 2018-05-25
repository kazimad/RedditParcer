package com.kazimad.reditparcer.models.error

import com.kazimad.reditparcer.tools.Logger
import java.lang.Exception

/**
 * Created by kazimad on 28.02.2018.
 */
class ResponseException(val errorMessege: String?) : Exception() {

    fun showCustomError(message: String? = null) {
        Logger.log("myLog", "ResponseException is : $message")
    }
}

