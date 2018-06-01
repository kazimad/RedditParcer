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


class MainActivity : AppCompatActivity() {
    private val PERMISSION_REQUESTS = 1
    private var loadImageBitmap: Bitmap? = null
    private var loadImageGif: GifDrawable? = null
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


    private fun getRequiredPermissions(): Array<String?> {
        return try {
            val info = this.packageManager.getPackageInfo(this.packageName, PackageManager.GET_PERMISSIONS)
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
            ActivityCompat.requestPermissions(this, allNeededPermissions.toTypedArray(), PERMISSION_REQUESTS)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (allPermissionsGranted()) {
            if (loadImageBitmap != null) {
                saveImage(loadImageBitmap!!)
            } else {
                saveGif(loadImageGif!!)
            }
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

    fun saveImage(image: Bitmap) {
        loadImageBitmap = image
        if (allPermissionsGranted()) {
            try {
                val millis = System.currentTimeMillis()
                MediaStore.Images.Media.insertImage(App.instance.contentResolver, image,
                        TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_TIME_DATE_PATTERN_1),
                        "some description")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            loadImageBitmap = null
        } else {
            getRuntimePermissions()
        }
    }

    fun saveGif(drawable: GifDrawable) {
        loadImageGif = drawable
        if (allPermissionsGranted()) {
            val byteBuffer = drawable.buffer
            val arr = ByteArray(byteBuffer.remaining())
            byteBuffer.get(arr)
            val file = createFile()
            try {
                val fos = FileOutputStream(file.absolutePath)
                fos.write(arr)
                fos.flush()
                fos.close()
            } catch (ioe: IOException) {
                ioe.printStackTrace()
            }
            loadImageGif = null
        } else {
            getRuntimePermissions()
        }
    }

    private fun createFile(): File {
        val millis = System.currentTimeMillis()
        val file = File(Environment.getExternalStorageDirectory().toString() + File.separator + TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_TIME_DATE_PATTERN_1) + ".gif")
        file.createNewFile()
        return file
    }
}
