package com.kazimad.reditparcer.models.error

class InnerError: Throwable() {
    var errorMessage: String? = null
    var code: Int = 0
    var throwable: Throwable? = null
}