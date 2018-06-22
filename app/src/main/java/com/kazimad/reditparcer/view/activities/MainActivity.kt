package com.kazimad.reditparcer.view.activities

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.interfaces.MainFragmentInterface
import com.kazimad.reditparcer.interfaces.MainInterface
import com.kazimad.reditparcer.models.error.ResponseException
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.Utils
import com.kazimad.reditparcer.view.fragments.ListResultFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class MainActivity : BaseActivity(), MainInterface {


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
                is UnknownHostException, is ConnectException, is SocketTimeoutException -> {
                    Logger.log("onMyError t is ${t.javaClass.canonicalName}")
                    showError(Utils.getResString(R.string.error_connection))
                    (supportFragmentManager.fragments.get(supportFragmentManager.backStackEntryCount - 1) as MainFragmentInterface).onLoadError()
                }
                else -> {
                    (t).printStackTrace()
                    showError(t.message)
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

    override fun onErrorCalled(t: Throwable?) {
        onMyError(t)
    }

    override fun onAddToFragmentStackCalled(fragment: Fragment, tag: String?) {
        addFragmentToStack(fragment, tag)
    }

    override fun onSaveImageClick(image: Bitmap) {
        saveImage(image)
    }
}
