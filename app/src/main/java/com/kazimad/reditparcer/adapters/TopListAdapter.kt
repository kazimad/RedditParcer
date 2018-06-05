package com.kazimad.reditparcer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.models.inner_models.ChildItemWrapper
import com.kazimad.reditparcer.tools.Utils
import com.kazimad.reditparcer.view.fragments.ListResultFragment
import kotlinx.android.synthetic.main.item_result_list.view.*

class TopListAdapter(var listener: onViewSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        var REGULAR_FLAG = 1
        var LOADING_FLAG = 2
    }

    private var topList: ArrayList<ChildItemWrapper> = ArrayList()

    inner class RegularViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_result_list, parent, false)) {
        fun bind(item: ChildItemWrapper) {

            if (item.childWrappedData!!.data.thumbnail.endsWith(".gif") || item.childWrappedData!!.data.thumbnail.endsWith(".gifv")) {
                var fixedGif = item.childWrappedData!!.data.thumbnail
                if (fixedGif.endsWith(".gifv")) {
                    fixedGif = item.childWrappedData!!.data.thumbnail.replace(".gifv", ".gif")
                }
                Glide.with(itemView.itemImage.context)
                        .asGif()
                        .load(fixedGif)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_broken_image)
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(itemView.itemImage)
            } else {
                Glide.with(itemView.itemImage.context)
                        .load(item.childWrappedData!!.data.thumbnail)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_broken_image)
                        )
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .into(itemView.itemImage)
            }
            itemView.titleText.text = item.childWrappedData!!.data.title
            itemView.textAuthor.text = item.childWrappedData!!.data.author
            itemView.createdAtText.text = Utils.getResString(R.string.text_created, Utils.getFriendlyTime(item.childWrappedData!!.data.createdUtc))
            itemView.commentsText.text = Utils.getResString(R.string.text_comments, item.childWrappedData?.data?.numComments!!)
            itemView.itemImage.setOnClickListener { listener.onItemSelected(item.childWrappedData!!.data.url) }
            if (item.flag == LOADING_FLAG) {
                itemView.loadingContainer.visibility = View.VISIBLE
            } else {
                itemView.loadingContainer.visibility = View.GONE
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return RegularViewHolder(parent)

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as RegularViewHolder).bind(getItems()[position])
    }

    override fun getItemCount() = topList.size

    fun getItems(): ArrayList<ChildItemWrapper> {
        return topList
    }

    fun addItems(items: List<ChildItemWrapper>) {
        val initPosition = topList.lastIndex
        if (topList.size >= items.size) {
            topList[initPosition].flag = REGULAR_FLAG
        }
        topList.addAll(items)
        if (topList.lastIndex < ListResultFragment.MAX_ITEMS_COUNT - 1) {
            topList[topList.lastIndex].flag = LOADING_FLAG
        }

        notifyDataSetChanged()
    }

    fun removeLoadingByForce() {
        if (!topList.isEmpty()) {
            topList[topList.lastIndex].flag = REGULAR_FLAG
            notifyDataSetChanged()
        }
    }

    interface onViewSelectedListener {
        fun onItemSelected(url: String?)
    }
}