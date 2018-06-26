package com.kazimad.reditparcer.extentions

import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.remote.ApiInterface
import com.kazimad.reditparcer.remote.ApiProvider
import com.kazimad.reditparcer.view_models.ListResultFViewModel


fun ListResultFViewModel.getApiInterface(): ApiInterface {
    return App.repository.getApiInterface()
}