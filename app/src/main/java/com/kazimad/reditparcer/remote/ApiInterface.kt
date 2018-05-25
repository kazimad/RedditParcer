package com.kazimad.reditparcer.remote

import com.kazimad.reditparcer.models.response.AuthResponse
import com.kazimad.reditparcer.models.response.TopResponse
import io.reactivex.Observable
import retrofit2.Response
import retrofit2.http.*

interface ApiInterface {


    @POST("/api/v1/access_token")
    fun authorize(@Path("client_id") clientId: String,
                  @Path("response_type") responseType: String,
                  @Path("state") state: String,
                  @Path("redirect_uri") redirectUri: String,
                  @Path("scope") scope: String): Observable<Response<AuthResponse>>

    @GET("/top.json")
    fun getList(@Query("after") after: String? = null,
                @Query("limit") limit: Int = 10): Observable<Response<TopResponse>>
}