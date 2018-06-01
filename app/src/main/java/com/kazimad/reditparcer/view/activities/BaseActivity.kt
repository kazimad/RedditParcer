package com.kazimad.reditparcer.view.activities

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Environment
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.TimeFormattingUtil
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*

abstract class BaseActivity : AppCompatActivity() {
    private val PERMISSION_REQUESTS = 1
    private var loadImageBitmap: Bitmap? = null
    private var loadImageGif: GifDrawable? = null

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