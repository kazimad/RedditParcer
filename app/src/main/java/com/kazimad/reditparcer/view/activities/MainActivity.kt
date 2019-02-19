package com.kazimad.reditparcer.view.activities

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.widget.Toast
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.interfaces.MainFragmentInterface
import com.kazimad.reditparcer.interfaces.MainInterface
import com.kazimad.reditparcer.tools.Utils
import com.kazimad.reditparcer.view.fragments.ListResultFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity(), MainInterface {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            addFragmentToStack(ListResultFragment())
        }
    }


    private fun addFragmentToStack(fragment: Fragment, tag: String? = null) {
        supportFragmentManager.beginTransaction()
                .add(this.container.id, fragment, fragment::class.java.canonicalName)
                .addToBackStack(fragment::class.java.canonicalName)
                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .commit()
    }

    private fun onMyError(t: Throwable?) {
        if (t != null) {
            val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = cm.activeNetworkInfo
            if (info == null || !info.isConnected) {
                showError(Utils.getResString(R.string.error_connection))
                (supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1] as MainFragmentInterface).onLoadError()
                return
            }
            (t).printStackTrace()
            showError(t.message)
            (supportFragmentManager.fragments[supportFragmentManager.backStackEntryCount - 1] as MainFragmentInterface).onLoadError()
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
