package com.kazimad.reditparcer.view.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.adapters.TopListAdapter
import com.kazimad.reditparcer.interfaces.listeners.EndlessRecyclerViewScrollListener
import com.kazimad.reditparcer.models.inner_models.ChildItemWrapper
import com.kazimad.reditparcer.models.response.ChildrenItem
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.Utils
import com.kazimad.reditparcer.view.activities.MainActivity
import com.kazimad.reditparcer.view_models.ListResultFViewModel
import kotlinx.android.synthetic.main.fragment_list_result.*


class ListResultFragment : BaseFragment(), TopListAdapter.onViewSelectedListener {

    companion object {
        const val MAX_ITEMS_COUNT: Int = 50
    }

    private lateinit var viewModel: ListResultFViewModel
    private lateinit var mActivity: Activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        Logger.log("ListResultFragment onViewCreated ")
        workOnRecycleView()
        viewModel = ViewModelProviders.of(this).get(ListResultFViewModel::class.java)
        viewModel.topLiveData.observe(this, Observer { onResultTopLiveData(it) })
        viewModel.errorLiveData.observe(this, Observer { (mActivity as MainActivity).onMyError(it) })
        if (!viewModel.loadedChildrenItems.isEmpty()) {
            Logger.log("workWithDataForRecycleView 11 onViewCreated")

            workWithDataForRecycleView(viewModel.loadedChildrenItems)
            topList.scrollToPosition(viewModel.lastPosition)
            viewModel.loadedChildrenItems.clear()
        } else {
            Logger.log("onLoadMore onViewCreated")
            viewModel.callListResults()
        }
    }

    private fun onResultTopLiveData(modelData: ArrayList<ChildrenItem>?) {
        Logger.log("onResultTopLiveData 11 onViewCreated")
        workWithDataForRecycleView(modelData)
    }

    private fun workWithDataForRecycleView(modelData: ArrayList<ChildrenItem>?) {
        val toAdapterList: ArrayList<ChildItemWrapper> = ArrayList()
        if (modelData != null) {
            for (i in 0 until modelData.size) {
                toAdapterList.add(ChildItemWrapper(TopListAdapter.REGULAR_FLAG, modelData[i]))
            }
            Logger.log("onResultTopLiveData toAdapterList.size ${toAdapterList.size}")
            (topList.adapter as TopListAdapter).addItems(toAdapterList)
            Logger.log("getItems().size ${(topList.adapter as TopListAdapter).getItems().size}")
            viewModel.topLiveData.postValue(null)
        }
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity!!
    }

    override fun onPause() {
        super.onPause()
        viewModel.lastPosition = (topList.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val itemsToSave = ArrayList<ChildrenItem>()
        for (i in 0 until (topList.adapter as TopListAdapter).getItems().size) {
            itemsToSave.add(ChildrenItem((topList.adapter as TopListAdapter).getItems()[i].childWrappedData?.data!!))
        }
        viewModel.loadedChildrenItems = itemsToSave
    }


    private fun workOnRecycleView() {
        topList.layoutManager = LinearLayoutManager(topList.context)
        topList.adapter = TopListAdapter(this)
        topList.addOnScrollListener(object : EndlessRecyclerViewScrollListener(topList.layoutManager as LinearLayoutManager) {
            override fun onLoadMore() {
                if (topList.adapter.itemCount <= MAX_ITEMS_COUNT) {
                    viewModel.callListResults((topList.adapter as TopListAdapter).getItems()[(topList.adapter as TopListAdapter).getItems().lastIndex].childWrappedData?.data?.name)
                } else {
                    Toast.makeText(topList.context, Utils.getResString(R.string.limit_items), Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    override fun onItemSelected(url: String?) {
        if (!url.isNullOrEmpty() || url!!.toLowerCase().endsWith(".png")
                || url.toLowerCase().endsWith(".jpeg") || url.toLowerCase().endsWith(".jpg")
                || url.toLowerCase().endsWith(".gif") || url.toLowerCase().endsWith(".gifv")
                || url.toLowerCase().endsWith(".bmp") || url.toLowerCase().endsWith(".webp")) {
            (mActivity as MainActivity).addFragmentToStack(ImageFragment.newInstance(url!!))
        } else {
            Toast.makeText(topList.context, Utils.getResString(R.string.error_no_image), LENGTH_LONG).show()
        }
    }
}