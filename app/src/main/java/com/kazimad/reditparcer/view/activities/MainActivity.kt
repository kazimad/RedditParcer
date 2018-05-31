package com.kazimad.reditparcer.view.activities

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
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
    //    private var fragmentListener: LoadImageCompleteListener? = null
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
            saveImage(loadImageBitmap!!)
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
//        fragmentListener = listener
        if (allPermissionsGranted()) {
            try {
                var millis = System.currentTimeMillis()
                MediaStore.Images.Media.insertImage(App.instance.contentResolver, image,
                        TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_TIME_DATE_PATTERN_1),
                        "some description")
//                fragmentListener!!.onImageLoaded()
            } catch (e: Exception) {
                e.printStackTrace()
//                fragmentListener!!.onImageLoaded(e.localizedMessage)
            }
            loadImageBitmap = null
//            fragmentListener = null
        } else {
            getRuntimePermissions()
        }
    }

    fun saveGif(drawable: GifDrawable) {
        Logger.log("MainAct saveGif ")
        var byteBufer = drawable.buffer
        val arr = ByteArray(byteBufer.remaining())
        byteBufer.get(arr)

        val file = createFile(App.instance)
        try {
            val fos = FileOutputStream(file.absolutePath)
            fos.write(arr)//gif is gif image object
            fos.flush()
            fos.close()
            Logger.log("MainAct saveGif finish ")

        } catch (ioe: IOException) {
            Logger.log("MainAct saveGif exception ")
            ioe.printStackTrace()
        }

    }
//    private class GetBitmapFromURLAsyncTask : AsyncTask<String, Void, Bitmap>() {
//        var listener: LoadImageCompleteListener? = null
//
//        override fun doInBackground(vararg url: String): Bitmap? {
//            return getBitmapFromURL(url[0])
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//            super.onPostExecute(result)
//            var millis = System.currentTimeMillis()
//            if (result != null) {
//                MediaStore.Images.Media.insertImage(App.instance.contentResolver, result,
//                        TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_DATE_PATTERN_13),
//                        "some description")
//                listener?.onImageLoaded()
//            } else {
//                listener?.onImageLoaded(Utils.getResString(R.string.load_error))
//            }
//        }
//
//        fun getBitmapFromURL(src: String): Bitmap? {
//            try {
//                val url = URL(src)
//                Logger.log("getBitmapFromURL url $url")
//                val connection = url.openConnection() as HttpURLConnection
//                connection.doInput = true
//                connection.connect()
//                val input = connection.inputStream
//                return BitmapFactory.decodeStream(input)
//            } catch (e: IOException) {
//                e.printStackTrace()
//                Logger.log("error is ${e.message}")
//                return null
//            }
//        }
//    }

    //    private class GetFigFromURLAsyncTask : AsyncTask<String, Void, Bitmap>() {
//        var listener: LoadImageCompleteListener? = null
//
//        override fun doInBackground(vararg url: String): Bitmap? {
//            return downloadFile(url[0])
//        }
//
//        override fun onPostExecute(result: Bitmap?) {
//            super.onPostExecute(result)
//            var millis = System.currentTimeMillis()
//            if (result != null) {
//                MediaStore.Images.Media.insertImage(App.instance.contentResolver, result,
//                        TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_DATE_PATTERN_13),
//                        "some description")
//                listener?.onImageLoaded()
//            } else {
//                listener?.onImageLoaded(Utils.getResString(R.string.load_error))
//            }
//        }
//
//        fun getBitmapFromURL(src: String): Bitmap? {
//            try {
//                val url = URL(src)
//                Logger.log("getBitmapFromURL url $url")
//                val connection = url.openConnection() as HttpURLConnection
//                connection.doInput = true
//                connection.connect()
//                val input = connection.inputStream
//                return BitmapFactory.decodeStream(input)
//            } catch (e: IOException) {
//                e.printStackTrace()
//                Logger.log("error is ${e.message}")
//                return null
//            }
//        }
//
//        fun downloadFile(context: Context, url: String?): String? {
//            if (url != null) {
//                try {
//                    val newPathFile = createFile(context)
//
//                    val urlConnection = URL(url).openConnection()
//
//                    val input = BufferedInputStream(urlConnection.getInputStream())
//                    val output = BufferedOutputStream(FileOutputStream(newPathFile))
//
//                    var i = 0
//                    while (i != -1) {
//                        output.write(i)
//                        i = input.read()
//                    }
//
//                    input.close()
//                    output.flush()
//                    output.close()
//                    return newPathFile
//
//                } catch (e: IOException) {
//                    e.printStackTrace()
//                }
//
//            }
//            return null
//
//        }
//
    fun createFile(context: Context?): File {
        var file: File?
        var millis = System.currentTimeMillis()
//        file = if (context != null) {
//        var name = StringBuilder()
//        name.append(context!!.externalCacheDir).append(TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_TIME_DATE_PATTERN_1)).append(".gif")
        file = File(context!!.externalCacheDir.parent  + File.separator + TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_TIME_DATE_PATTERN_1)+".gif")
//        }
//        else {
//            File(Environment.getExternalStorageDirectory().toString() + TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_DATE_PATTERN_13) + ".gif")
//        }
        Logger.log("createFile file.path is ${file.path}")
        Logger.log("createFile file.path is ${file.name}")
        file.createNewFile()
        return file
    }
//    }
}
