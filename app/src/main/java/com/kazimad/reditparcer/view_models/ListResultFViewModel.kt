package com.kazimad.reditparcer.view_models

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.kazimad.reditparcer.extentions.getListWithData
import com.kazimad.reditparcer.models.response.ChildrenItem


class ListResultFViewModel : ViewModel() {
    var topLiveData: MutableLiveData<ArrayList<ChildrenItem>?> = MutableLiveData()
    var errorLiveData: MutableLiveData<Throwable> = MutableLiveData()
    var loadedChildrenItems = ArrayList<ChildrenItem>()
    var lastPosition: Int = 0

    fun callListResults(after: String? = null, lastVisiblePosition: Int = 0, limit: Int = 10) {
        lastPosition = lastVisiblePosition
        getListWithData(after, limit)?.subscribe({ result ->
            topLiveData.value = result.children as ArrayList<ChildrenItem>
        }) { error ->
            errorLiveData.value = Throwable(error.localizedMessage)
            error.printStackTrace()
        }
    }
}