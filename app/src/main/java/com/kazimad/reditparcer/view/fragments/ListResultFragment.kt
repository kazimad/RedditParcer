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
import com.kazimad.reditparcer.R
import com.kazimad.reditparcer.adapters.TopListAdapter
import com.kazimad.reditparcer.models.response.TopResponse
import com.kazimad.reditparcer.view.activities.MainActivity
import com.kazimad.reditparcer.view_models.ListResultFViewModel
import com.kazimad.reditparcer.tools.Logger
import kotlinx.android.synthetic.main.fragment_list_result.*

class ListResultFragment : Fragment() {
    private lateinit var viewModel: ListResultFViewModel
    private lateinit var mActivity : Activity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProviders.of(this).get(ListResultFViewModel::class.java)
        viewModel.topLiveData.observe(this, Observer { onResultTopLiveData(it!!) })
        viewModel.errorLiveData.observe(this, Observer {(mActivity as MainActivity).onMyError(it) })
        viewModel.callListResults()

    }

    fun onResultTopLiveData(topResponse: TopResponse){
        Logger.log("onResultTopLiveData ${topResponse.data.children!![0].data.author}")
        topList.layoutManager = LinearLayoutManager(topList.context)
        topList.adapter = TopListAdapter(topResponse.data.children)
    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mActivity = this.activity!!
    }
}