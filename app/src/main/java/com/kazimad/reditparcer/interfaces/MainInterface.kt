package com.kazimad.reditparcer.interfaces

import android.graphics.Bitmap
import android.support.v4.app.Fragment

interface MainInterface {
    fun onSaveImageClick(image: Bitmap)
    fun onAddToFragmentStackCalled(fragment: Fragment, tag: String? = null)
    fun onErrorCalled(t: Throwable?)
}