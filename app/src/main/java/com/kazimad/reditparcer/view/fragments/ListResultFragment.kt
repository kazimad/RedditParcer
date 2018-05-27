package com.kazimad.reditparcer.view.fragments

import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toast.LENGTH_LONG
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.adapters.TopListAdapter
import com.kazimad.reditparcer.models.inner_models.ChildItemWrapper
import com.kazimad.reditparcer.models.response.TopResponse
import com.kazimad.reditparcer.tools.Logger
import com.kazimad.reditparcer.tools.Utils
import com.kazimad.reditparcer.tools.listeners.EndlessRecyclerViewScrollListener
import com.kazimad.reditparcer.view.activities.MainActivity
import com.kazimad.reditparcer.view_models.ListResultFViewModel
import kotlinx.android.synthetic.main.fragment_list_result.*


class ListResultFragment : Fragment(), TopListAdapter.onViewSelectedListener {


    private lateinit var viewModel: ListResultFViewModel
    private lateinit var mActivity: Activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        workOnRecycleView()
        viewModel = ViewModelProviders.of(this).get(ListResultFViewModel::class.java)
        viewModel.topLiveData.observe(this, Observer { onResultTopLiveData(it!!) })
        viewModel.errorLiveData.observe(this, Observer { (mActivity as MainActivity).onMyError(it) })
        viewModel.callListResults()
    }

    private fun onResultTopLiveData(topResponse: TopResponse) {
        Logger.log("onResultTopLiveData last loadaed item name ${topResponse.data.children!![topResponse.data.children.lastIndex].data?.name}")
        val toAdapterList: ArrayList<ChildItemWrapper> = ArrayList()
        for (i in 0 until topResponse.data.children.size) {
            toAdapterList.add(ChildItemWrapper(TopListAdapter.REGULAR_FLAG, topResponse.data.children[i]))
        }
        Logger.log("onResultTopLiveData toAdapterList.size ${toAdapterList.size}")
        (topList.adapter as TopListAdapter).addItems(toAdapterList)
        Logger.log("onResultTopLiveData size is ${(topList.adapter as TopListAdapter).getItems().size}")
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity!!
    }

    private fun workOnRecycleView() {
        topList.layoutManager = LinearLayoutManager(topList.context)
        topList.adapter = TopListAdapter(this)
        topList.addOnScrollListener(object : EndlessRecyclerViewScrollListener(topList.layoutManager as LinearLayoutManager) {
            override fun onLoadMore() {
                viewModel.callListResults((topList.adapter as TopListAdapter).getItems()[(topList.adapter as TopListAdapter).getItems().lastIndex - 1].childWrappedData?.data?.name)
                Logger.log("onLoadMore last item name ${(topList.adapter as TopListAdapter).getItems()[(topList.adapter as TopListAdapter).getItems().lastIndex - 1].childWrappedData?.data?.name}")
            }
        })
    }

    override fun onItemSelected(url: String?) {
        if (url.isNullOrEmpty()) {
            Toast.makeText(topList.context, Utils.getResString(R.string.error_no_image), LENGTH_LONG).show()
        } else {
            (mActivity as MainActivity).addFragmentToStack(ImageFragment.newInstance(url!!))
        }
    }
}