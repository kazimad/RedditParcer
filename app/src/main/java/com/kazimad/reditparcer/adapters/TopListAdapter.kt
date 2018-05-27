package com.kazimad.reditparcer.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.models.inner_models.ChildItemWrapper
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.Utils
import kotlinx.android.synthetic.main.item_result_list.view.*

class TopListAdapter(var listener: onViewSelectedListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        var REGULAR_FLAG = 1
        var LOADING_FLAG = 2
    }

    private var topList: ArrayList<ChildItemWrapper> = ArrayList()

    inner class RegularViewHolder(private var parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_result_list, parent, false)) {

        fun bind(item: ChildItemWrapper, position: Int) {

            if (item.childWrappedData!!.data.thumbnail.endsWith(".gif")) {
                Glide.with(itemView.itemImage.context)
                        .asGif()
                        .load(item.childWrappedData!!.data.thumbnail)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_broken_image)
                        )
                        .into(itemView.itemImage)
            } else {
                Glide.with(itemView.itemImage.context)
                        .load(item.childWrappedData!!.data.thumbnail)
                        .apply(RequestOptions()
                                .placeholder(R.drawable.ic_place_holder)
                                .error(R.drawable.ic_broken_image)
                        )
                        .into(itemView.itemImage)
            }
            itemView.titleText.text = item.childWrappedData!!.data.title
            itemView.textAuthor.text = item.childWrappedData!!.data.author
            itemView.createdAtText.text = Utils.getResString(R.string.text_created, Utils.getFriendlyTime(item.childWrappedData!!.data.createdUtc))
            itemView.commentsText.text = Utils.getResString(R.string.text_comments, item.childWrappedData?.data?.numComments!!)
            itemView.itemImage.setOnClickListener { listener.onItemSelected(item.childWrappedData!!.data.url) }
            if (position == itemCount - 1) {
                Logger.log("TopListAdapter bind position == itemCount - 1 ")
            }
        }
    }

    inner class LoadingViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_loading_result_list, parent, false)) {
        fun bind() {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if (viewType == REGULAR_FLAG) {
            RegularViewHolder(parent)
        } else {
            LoadingViewHolder(parent)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Logger.log("onBindViewHolder getItemViewType(position) ${getItemViewType(position)},position ${position}")
        if (getItemViewType(position) == REGULAR_FLAG) {
            (holder as RegularViewHolder).bind(getItems()[position], position)
        } else {
            (holder as LoadingViewHolder).bind()
        }
    }

    override fun getItemViewType(position: Int): Int {
        Logger.log("getItemViewType topList.lastIndex ${topList.lastIndex},position ${position}")
        return if (position < topList.lastIndex) {
            REGULAR_FLAG
        } else {
            LOADING_FLAG
        }
    }

    override fun getItemCount() = topList.size

    fun getItems(): List<ChildItemWrapper> {
        return topList
    }

    fun addItems(items: List<ChildItemWrapper>) {
        val initPosition = items.lastIndex
        if (topList.size >= items.size) {
            topList.removeAt(initPosition)
            notifyItemRemoved(initPosition)
        }

        topList.addAll(items)
        topList.add(ChildItemWrapper(LOADING_FLAG))
        notifyItemRangeChanged(initPosition, items.size + 1)
    }

    private fun getLastPosition() = if (topList.lastIndex == -1) 0 else topList.lastIndex

    interface onViewSelectedListener {
        fun onItemSelected(url: String?)
    }
}