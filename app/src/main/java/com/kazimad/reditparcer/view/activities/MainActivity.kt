package com.kazimad.reditparcer.view.activities

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.models.error.ResponseException
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.view.fragments.ListResultFragment
import java.net.ConnectException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addFragmentToStack(ListResultFragment())
    }


    fun addFragmentToStack(fragment: Fragment, tag: String? = null) {
        supportFragmentManager.beginTransaction().add(fragment, tag).commit()
    }

    fun onMyError(t: Throwable?) {
        if (t != null) {
            when (t) {
                is ResponseException -> {
                    showError(t.message)
                    finish()
                }
                is ConnectException -> {
                    showError(t.message)
                    finish()
                }
                else -> {
                    Logger.log("ErrorObserver else ${t.javaClass.canonicalName}")
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
}
