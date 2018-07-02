package com.kazimad.reditparcer.remote

import android.arch.lifecycle.MutableLiveData
import com.kazimad.reditparcer.models.response.ChildrenItem
import com.kazimad.reditparcer.models.response.TopResponse
import com.kazimad.reditparcer.remote.ApiRepository.Companion.baseUrl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class Repository(private val apiRepository: ApiRepository) {



    fun getListWithData(after: String?, limit: Int, topLiveData: MutableLiveData<*>,
                        errorLiveData: MutableLiveData<*>) {
        apiRepository.create(baseUrl).getList(after, limit)
                .filter(ApiHelper.baseApiFilterPredicate(TopResponse::class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    topLiveData.value = result.body()!!.data.children as ArrayList<ChildrenItem>
                }, { error ->
                    error.printStackTrace()
                    errorLiveData.value = error
                })
    }
}