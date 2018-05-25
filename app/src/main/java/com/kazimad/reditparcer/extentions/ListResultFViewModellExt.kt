package com.kazimad.reditparcer.extentions

import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.remote.ApiProvider
import com.kazimad.reditparcer.tools.Prefs
import com.kazimad.reditparcer.view_models.ListResultFViewModel


fun ListResultFViewModel.getApiProvider(): ApiProvider {
    return App.repository.getApiProvider()
}

fun ListResultFViewModel.getPrefs(): Prefs {
    return App.repository.getPrefs()
}