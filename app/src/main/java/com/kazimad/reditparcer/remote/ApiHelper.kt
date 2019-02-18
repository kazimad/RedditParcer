package com.kazimad.reditparcer.remote

import com.kazimad.reditparcer.models.error.ResponseException
import com.kazimad.reditparcer.tools.Logger
import io.reactivex.functions.Predicate
import retrofit2.Response
import java.net.HttpURLConnection

class ApiHelper {

    companion object {
        private const val RESPONSE_CODE_BAD_REQUESTS = HttpURLConnection.HTTP_BAD_REQUEST
        fun <T : Any> baseApiFilterPredicate(): Predicate<Response<T>> {
            return Predicate {
                if (it.code() >= RESPONSE_CODE_BAD_REQUESTS) {
                    throw Throwable(it.message())
                }
                if (it.body() == null) {
                    throw ResponseException(it.message())
                }
                true
            }
        }
    }
}