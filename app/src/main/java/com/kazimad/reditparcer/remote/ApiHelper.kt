package com.kazimad.reditparcer.remote

import com.kazimad.reditparcer.models.error.ResponseException
import com.kazimad.reditparcer.models.response.TopResponse
import com.kazimad.reditparcer.tools.Logger
import io.reactivex.functions.Predicate
import retrofit2.Response
import java.net.HttpURLConnection
import kotlin.reflect.KClass

class ApiHelper {

    companion object {
        private const val RESPONSE_CODE_BAD_REQUESTS = HttpURLConnection.HTTP_BAD_REQUEST
        fun <T : Any> baseApiFilterPredicate(type: KClass<T>): Predicate<Response<T>> {
            return Predicate({
                if (it.code() >= RESPONSE_CODE_BAD_REQUESTS) {
                    throw Throwable(it.message())
                }
                if (it.body() == null) {
                    Logger.log("ApiHelper baseApiFilterPredicate if (it.body() == null) ")
                    throw ResponseException(it.message())
                }
                true
            })
        }
    }
}