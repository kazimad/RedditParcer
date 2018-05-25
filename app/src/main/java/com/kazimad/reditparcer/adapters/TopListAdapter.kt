package com.kazimad.reditparcer.adapters

//import kotlinx.android.synthetic.main.item_result_list.view.*
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.models.response.ChildrenItem
import com.kazimad.reditparcer.tools.Picasser
import com.kazimad.reditparcer.tools.Utils
import kotlinx.android.synthetic.main.item_result_list.view.*

class TopListAdapter(private val favoriteList: List<ChildrenItem>) : RecyclerView.Adapter<TopListAdapter.ViewHolder>() {

    inner class ViewHolder(var parent: ViewGroup) : RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_result_list, parent, false)) {
        fun bind(item: ChildrenItem) {
            Picasser.showAndError(parent.context, item.data.thumbnail, R.drawable.ic_error, parent.itemImage)
            parent.titleText.text = item.data.title
            parent.textAuthor.text = item.data.author
            parent.textAuthor.createdAtText.text = Utils.getResString(R.string.text_created, Utils.getFriendlyTime(item.data.createdUtc))
            parent.textAuthor.commentsText.text = Utils.getResString(R.string.text_comments, item.data.numComments)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopListAdapter.ViewHolder {
        return ViewHolder(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItems()[position])
    }

    override fun getItemCount() = favoriteList.size

    private fun getItems(): List<ChildrenItem> {
        return favoriteList
    }


}