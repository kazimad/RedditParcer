package com.kazimad.reditparcer.remote

import com.kazimad.reditparcer.models.response.Data
import com.kazimad.reditparcer.models.response.TopResponse
import com.kazimad.reditparcer.remote.ApiProvider.Companion.baseUrl
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


class UsersRepository(private val apiProvider: ApiProvider) {

    fun getListWithData(after: String?, limit: Int): Observable<Data>? {
        return apiProvider.create(baseUrl).getList(after, limit)
                .filter(ApiHelper.baseApiFilterPredicate(TopResponse::class))
                .subscribeOn(Schedulers.io())
                .flatMap { getDataFromResponse(it.body()!!) }
                .observeOn(AndroidSchedulers.mainThread())

    }

    private fun getDataFromResponse(topResponse: TopResponse): Observable<Data> {
        return Observable.fromArray(topResponse.data)
    }
}