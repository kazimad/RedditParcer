package com.kazimad.reditparcer.view.fragments

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.tools.BUNDLE_PARAM
import com.kazimad.reditparcer.tools.listeners.LoadImageCompleteListener
import com.kazimad.reditparcer.view.activities.MainActivity
import kotlinx.android.synthetic.main.fragment_image.*

/**
 * Created by Kazimad on 27.05.2018.
 */
class ImageFragment : Fragment(), LoadImageCompleteListener {


    private lateinit var mActivity: Activity
    private lateinit var targetUrl: String

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
            if (targetUrl.endsWith(".gif")) {
                Glide.with(bigImage)
                        .asGif()
                        .load(targetUrl)
                        .apply(RequestOptions()
//                                .placeholder(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_broken_image)
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(object : RequestListener<GifDrawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<GifDrawable>?, isFirstResource: Boolean): Boolean {
                                imageProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(resource: GifDrawable?, model: Any?, target: Target<GifDrawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                imageProgress.visibility = View.GONE
                                return false
                            }
                        })
                        .into(bigImage)
            } else {
                Glide.with(bigImage.context)
                        .load(targetUrl)
                        .apply(RequestOptions()
//                                .placeholder(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_broken_image)
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                imageProgress.visibility = View.GONE
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                imageProgress.visibility = View.GONE
                                return false
                            }
                        })
                        .into(bigImage)
            }
        }
        loadButton.setOnClickListener(object : View.OnClickListener {
            override fun onClick(p0: View?) {
                loadButtonClick()
            }
        })
    }

    fun loadButtonClick() {
        (mActivity as MainActivity).loadImage(targetUrl, this)
        loadButton.isEnabled = false
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity!!
    }

    override fun onImageLoaded(error: String?) {
        loadButton.isEnabled = true
    }
}