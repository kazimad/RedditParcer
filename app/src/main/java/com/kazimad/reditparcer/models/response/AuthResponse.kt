package com.kazimad.reditparcer.models.response

data class AuthResponse (
        //TODO remove class
        val accessToken: String? = null,
        val scope: String? = null,
        val tokenType: String? = null,
        val expiresIn: String? = null
)