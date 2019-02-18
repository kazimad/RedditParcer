package com.kazimad.reditparcer.models.error

import java.lang.Exception

/**
 * Created by kazimad on 28.02.2018.
 */
class ResponseException(val errorMessage: String?) : Exception()

