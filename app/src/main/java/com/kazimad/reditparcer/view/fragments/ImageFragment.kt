package com.kazimad.reditparcer.view.fragments

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.interfaces.MainAppContext
import com.kazimad.reditparcer.tools.BUNDLE_PARAM
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.view.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_image.*

/**
 * Created by Kazimad on 27.05.2018.
 */
class ImageFragment : Fragment(), MainAppContext {

    private lateinit var mActivity: Activity
    private lateinit var targetUrl: String
    private var cashedBitmap: Bitmap? = null
    private var cashedGif: GifDrawable? = null

    companion object {
        fun newInstance(url: String): ImageFragment {
            val result = ImageFragment()
            val bundle = Bundle(1)
            bundle.putString(BUNDLE_PARAM, url)
            result.arguments = bundle
            return result
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_image, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (arguments != null) {
            var fixedGif = ""
            targetUrl = arguments!!.getString(BUNDLE_PARAM)

            if (targetUrl.endsWith(".gif") || targetUrl.endsWith(".gifv")) {
                var fixedGif = targetUrl
                if (fixedGif.endsWith(".gifv")) {
                    fixedGif = targetUrl.replace(".gifv", ".gif")
                }
                Glide.with(bigImage)
                        .asGif()
                        .load(fixedGif)
                        .apply(RequestOptions()
                                .error(R.drawable.ic_broken_image)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(object : RequestListener<GifDrawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                                imageProgress.visibility = View.GONE
                                loadButton.isEnabled = false
                                Toast.makeText(bigImage.context, e?.message, Toast.LENGTH_LONG).show()
                                return false
                            }

                            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                loadButton.isEnabled = true
                                imageProgress.visibility = View.GONE
                                cashedGif = resource
                                return false
                            }
                        })
                        .into(bigImage)
            } else {
                Glide.with(bigImage.context)
                        .asBitmap()
                        .load(targetUrl)
                        .apply(RequestOptions()
                                .error(R.drawable.ic_broken_image)
                        )
                        .transition(BitmapTransitionOptions.withCrossFade())
                        .listener(object : RequestListener<Bitmap> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Bitmap>?, isFirstResource: Boolean): Boolean {
                                imageProgress.visibility = View.GONE
                                loadButton.isEnabled = false
                                return false
                            }

                            override fun onResourceReady(resource: Bitmap?, model: Any?, target: Target<Bitmap>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                imageProgress.visibility = View.GONE
                                loadButton.isEnabled = true
                                cashedBitmap = resource
                                return false
                            }
                        })
                        .into(bigImage)
            }
        }
        loadButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                // had done like in telegram, with out callback
                Thread(Runnable { loadButtonClick() }).start()
            }
        })
    }

    fun loadButtonClick() {
        if (cashedBitmap != null) {
            (mActivity as MainActivity).saveImage(cashedBitmap!!)
        }
        if (cashedGif != null) {
            (mActivity as MainActivity).saveGif(cashedGif!!)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity!!
    }

    override fun onLoadError() {
        Logger.log("ImageFragment onLoadError")
    }
}