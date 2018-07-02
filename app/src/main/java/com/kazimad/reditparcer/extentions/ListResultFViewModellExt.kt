package com.kazimad.reditparcer.extentions

import android.arch.lifecycle.MutableLiveData
import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.remote.ApiInterface
import com.kazimad.reditparcer.view_models.ListResultFViewModel


fun ListResultFViewModel.getListWithData(after: String?, limit: Int, topLiveData: MutableLiveData<*>,
                                         errorLiveData: MutableLiveData<*>) {
    return App.repository.getListWithData(after, limit, topLiveData, errorLiveData)
}