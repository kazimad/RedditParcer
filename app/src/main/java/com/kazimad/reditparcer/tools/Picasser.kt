package com.kazimad.reditparcer.tools

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.squareup.picasso.Target
import java.io.File

class Picasser {

    companion object {
        fun showAndError(context: Context, urlImage: String, drawable: Int, imageView: ImageView) {
            Picasso.with(context).load(urlImage).error(drawable).into(imageView)
        }

        fun showAndError(context: Context, file: File, drawable: Int, imageView: ImageView) {
            Picasso.with(context).load(file).error(drawable).into(imageView)
        }

        fun showAndErrorCallback(context: Context, urlImage: String, imageView: ImageView, callback: Callback) {
            Picasso.with(context).load(urlImage).into(imageView, callback)
        }

        fun show(context: Context, urlImage: String, imageView: ImageView) {
            Picasso.with(context).load(urlImage).into(imageView)
        }

        fun show(context: Context, file: File, imageView: ImageView) {
            Picasso.with(context).load(file).into(imageView)
        }

        fun show(context: Context, resourceId: Int, imageView: ImageView) {
            Picasso.with(context).load(resourceId).into(imageView)
        }

        fun show_fit(context: Context, resourceURL: String, imageView: ImageView) {
            Picasso.with(context)
                    .load(resourceURL)
                    .into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                            imageView.background = BitmapDrawable(context.resources, bitmap)
                        }

                        override fun onBitmapFailed(errorDrawable: Drawable) {}

                        override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
                    })
        }

        fun show_fit(context: Context, resId: Int, imageView: ImageView) {
            Picasso.with(context)
                    .load(resId)
                    .into(object : Target {
                        override fun onBitmapLoaded(bitmap: Bitmap, from: Picasso.LoadedFrom) {
                            imageView.background = BitmapDrawable(context.resources, bitmap)
                        }

                        override fun onBitmapFailed(errorDrawable: Drawable) {}

                        override fun onPrepareLoad(placeHolderDrawable: Drawable) {}
                    })
        }
    }
}