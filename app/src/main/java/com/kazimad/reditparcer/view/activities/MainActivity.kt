package com.kazimad.reditparcer.view.activities

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.models.error.InnerError
import com.kazimad.reditparcer.models.error.ResponseException
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.TimeFormattingUtil
import com.kazimad.reditparcer.view.fragments.ListResultFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.ConnectException
import java.util.*


class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            addFragmentToStack(ListResultFragment())
        }
    }


    fun addFragmentToStack(fragment: Fragment, tag: String? = null) {
        supportFragmentManager.beginTransaction()
                .add(this.container.id, fragment, fragment::class.java.canonicalName)
                .addToBackStack(fragment::class.java.canonicalName)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .commit()
    }

    fun onMyError(t: Throwable?) {
        if (t != null) {
            when (t) {
                is ResponseException -> {
                    showError(t.errorMessege)
                    finish()
                }
                is ConnectException -> {
                    showError(t.message)
                    finish()
                }
                else -> {
                    (t).printStackTrace()
                    showError((t as InnerError).errorMessage)
                    finish()
                }
            }
        }
    }

    private fun showError(errorMessage: String?) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
    }


    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }
}
