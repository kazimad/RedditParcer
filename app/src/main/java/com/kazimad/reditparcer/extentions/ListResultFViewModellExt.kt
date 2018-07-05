package com.kazimad.reditparcer.extentions

import com.kazimad.reditparcer.App.Companion.mainComponent
import com.kazimad.reditparcer.models.response.Data
import com.kazimad.reditparcer.view_models.ListResultFViewModel
import io.reactivex.Observable


fun ListResultFViewModel.getListWithData(after: String?, limit: Int): Observable<Data>? {
    return mainComponent.getUserRepository().getListWithData(after, limit)
}