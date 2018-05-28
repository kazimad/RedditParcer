package com.kazimad.reditparcer.view.activities

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.models.error.ResponseException
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.TimeFormattingUtil
import com.kazimad.reditparcer.tools.Utils
import com.kazimad.reditparcer.tools.listeners.LoadImageCompleteListener
import com.kazimad.reditparcer.view.fragments.ListResultFragment
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.net.ConnectException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUESTS = 1
    private lateinit var result: Bitmap
    private var loadImageStr: String? = null
    private var fragmentListener: LoadImageCompleteListener? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            addFragmentToStack(ListResultFragment())
        }
    }


    fun addFragmentToStack(fragment: Fragment, tag: String? = null) {
        Logger.log("addFragmentToStack MainActivity ${fragment::class.java.canonicalName}")
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


    private fun getRequiredPermissions(): Array<String?> {
        return try {
            val info = this.packageManager
                    .getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
            val ps = info.requestedPermissions
            if (ps != null && ps.isNotEmpty()) {
                ps
            } else {
                arrayOfNulls(0)
            }
        } catch (e: Exception) {
            arrayOfNulls(0)
        }

    }

    private fun allPermissionsGranted(): Boolean {
        for (permission in getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission!!)) {
                return false
            }
        }
        return true
    }

    private fun getRuntimePermissions() {
        val allNeededPermissions = ArrayList<String>()
        for (permission in getRequiredPermissions()) {
            if (!isPermissionGranted(this, permission!!)) {
                allNeededPermissions.add(permission)
            }
        }

        if (!allNeededPermissions.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (allPermissionsGranted()) {
            loadImage(loadImageStr!!, fragmentListener!!)
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            Logger.log("Permission granted: $permission")
            return true
        }
        Logger.log("Permission NOT granted: $permission")
        return false
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if (count == 1) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    fun loadImage(url: String, fragmentListener: LoadImageCompleteListener) {
        loadImageStr = url
        this.fragmentListener = fragmentListener
        if (allPermissionsGranted()) {
            val bitmapFromURLAsyncTask = GetBitmapFromURLAsyncTask()
            bitmapFromURLAsyncTask.execute(loadImageStr)
            bitmapFromURLAsyncTask.listener = object : LoadImageCompleteListener {
                override fun onImageLoaded(error: String?) {
                    if (error != null) {
                        Toast.makeText(App.instance, error, Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(App.instance, Utils.getResString(R.string.image_loaded), Toast.LENGTH_LONG).show()
                    }
                    loadImageStr = null
                    fragmentListener.onImageLoaded()
                }
            }
        } else {
            getRuntimePermissions()
        }

    }

    private class GetBitmapFromURLAsyncTask : AsyncTask<String, Void, Bitmap>() {
        var listener: LoadImageCompleteListener? = null

        override fun doInBackground(vararg url: String): Bitmap? {
            return getBitmapFromURL(url[0])
        }

        override fun onPostExecute(result: Bitmap?) {
            super.onPostExecute(result)
            var millis = System.currentTimeMillis()
            if (result != null) {
                MediaStore.Images.Media.insertImage(App.instance.contentResolver, result,
                        TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_DATE_PATTERN_13),
                        "some description")
                listener?.onImageLoaded()
            } else {
                listener?.onImageLoaded(Utils.getResString(R.string.load_error))
            }
        }

        fun getBitmapFromURL(src: String): Bitmap? {
            return try {
                val url = URL(src)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream
                BitmapFactory.decodeStream(input)
            } catch (e: IOException) {
                e.printStackTrace()
                null
            }
        }
    }
}
