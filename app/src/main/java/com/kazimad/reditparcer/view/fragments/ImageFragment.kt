package com.kazimad.reditparcer.view.fragments

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.interfaces.MainInterface
import com.kazimad.reditparcer.tools.BUNDLE_PARAM
import kotlinx.android.synthetic.main.fragment_image.*

/**
 * Created by Kazimad on 27.05.2018.
 */
class ImageFragment : Fragment() {

    private lateinit var mainInterface: MainInterface
    private lateinit var targetUrl: String
    private var cashedBitmap: Bitmap? = null

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
            targetUrl = arguments!!.getString(BUNDLE_PARAM)
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
        loadButton.setOnClickListener {
            Thread(Runnable { onSaveButtonClick() }).start()
        }
    }

    private fun onSaveButtonClick() {
        mainInterface.onSaveImageClick(cashedBitmap!!)

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        val mActivity: Activity

        if (context is Activity) {
            mActivity = context
            try {
                mainInterface = mActivity as MainInterface
            } catch (e: ClassCastException) {
                throw ClassCastException(activity.toString() + " must implement MainInterface")
            }
        }
    }
}