package com.kazimad.reditparcer.view.activities

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Handler
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.tools.TimeFormattingUtil
import com.kazimad.reditparcer.tools.Utils
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    private val PERMISSION_REQUESTS = 1
    private var loadImageBitmap: Bitmap? = null
    private var dontAskAgainChecked: Boolean = false
    var handler = Handler()
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
            saveImage(loadImageBitmap!!)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (i in 0 until permissions.size) {
                if (permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE || permissions[i] == Manifest.permission.WRITE_EXTERNAL_STORAGE) {
                    // because android asks it in one dialog
                    dontAskAgainChecked = !shouldShowRequestPermissionRationale(permissions[i]) && grantResults[i] == PackageManager.PERMISSION_DENIED
                }
            }
            if (dontAskAgainChecked) {
                Toast.makeText(App.instance, Utils.getResString(R.string.text_allow), Toast.LENGTH_LONG).show()
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    private fun isPermissionGranted(context: Context, permission: String): Boolean {
        if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED) {
            return true
        }
        return false
    }


    fun saveImage(image: Bitmap) {
        loadImageBitmap = image
        if (allPermissionsGranted()) {
            try {
                val millis = System.currentTimeMillis()
                MediaStore.Images.Media.insertImage(App.instance.contentResolver, image,
                        TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_TIME_DATE_PATTERN_1),
                        "some description")
                handler.post { Toast.makeText(this, getString(R.string.image_loaded), Toast.LENGTH_LONG).show() }
            } catch (e: Exception) {
                e.printStackTrace()
                handler.post { Toast.makeText(this, getString(R.string.load_error), Toast.LENGTH_LONG).show() }
            }
            loadImageBitmap = null
        } else {
            getRuntimePermissions()
        }
    }
}