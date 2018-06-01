package com.kazimad.reditparcer.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.kazimad.reditparcer.extentions.getApiProvider
import com.kazimad.reditparcer.models.error.InnerError
import com.kazimad.reditparcer.models.response.ChildrenItem
import com.kazimad.reditparcer.models.response.TopResponse
import com.kazimad.reditparcer.remote.ApiHelper
import com.kazimad.reditparcer.remote.ApiProvider
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class ListResultFViewModel : ViewModel() {
    var topLiveData: MutableLiveData<ArrayList<ChildrenItem>?> = MutableLiveData()
    var errorLiveData: MutableLiveData<InnerError> = MutableLiveData()
    var loadedChildrenItems = ArrayList<ChildrenItem>()
    var lastPosition: Int = 0

    fun callListResults(after: String? = null, lastVisiblePosition: Int = 0, limit: Int = 10) {
        lastPosition = lastVisiblePosition
        getApiProvider().create(ApiProvider.baseUrl).getList(after, limit)
                .filter(ApiHelper.baseApiFilterPredicate(TopResponse::class))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ result ->
                    topLiveData.value = result.body()!!.data.children as ArrayList<ChildrenItem>
                }, { error ->
                    error.printStackTrace()
                    val innerError = InnerError()
                    innerError.errorMessage = error?.localizedMessage
                    innerError.throwable = error?.cause
                    errorLiveData.value = innerError
                })
    }
}