package com.kazimad.reditparcer.view.fragments

import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v4.app.Fragment
import com.kazimad.reditparcer.App
import com.kazimad.reditparcer.interfaces.listeners.LoadImageCompleteListener
import com.kazimad.reditparcer.tools.TimeFormattingUtil


abstract class BaseFragment : Fragment() {

    open fun saveImage(image: Bitmap, listener: LoadImageCompleteListener) {
//        var savedImagePath: String? = null
//
//        val imageFileName = "JPEG_" + "FILE_NAME" + ".jpg"
//        val storageDir = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/YOUR_FOLDER_NAME")
//        var success = true
//        if (!storageDir.exists()) {
//            success = storageDir.mkdirs()
//        }
//        if (success) {
//            val imageFile = File(storageDir, imageFileName)
//            savedImagePath = imageFile.getAbsolutePath()
//            try {
//                val fOut = FileOutputStream(imageFile)
//                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut)
//                fOut.close()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//
//            // Add the image to the system gallery
//            galleryAddPic(savedImagePath)
//            listener.onImageLoaded()
//
//        }
        try {
            var millis = System.currentTimeMillis()
            MediaStore.Images.Media.insertImage(App.instance.contentResolver, image,
                    TimeFormattingUtil.formatDateWithPattern(millis, TimeFormattingUtil.DISPLAY_DATE_PATTERN_13),
                    "some description")
            listener.onImageLoaded()

        } catch (e: Exception) {
            e.printStackTrace()
            listener.onImageLoaded(e.localizedMessage)

        }

//        return savedImagePath
    }

//    open fun galleryAddPic(imagePath: String?):Intent {
//        val mediaScanIntent = Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE)
//        val f = File(imagePath)
//        val contentUri = Uri.fromFile(f)
//        mediaScanIntent.data = contentUri
//        return mediaScanIntent
//    }
}