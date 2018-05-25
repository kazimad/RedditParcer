package com.kazimad.reditparcer.models.error

class InnerError: Exception() {
    var errorMessage: String? = null
    var code: Int = 0
    var throwable: Throwable? = null
}