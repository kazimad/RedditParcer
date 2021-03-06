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
import com.kazimad.reditparcer.interfaces.MainFragmentInterface
import com.kazimad.reditparcer.interfaces.MainInterface
import com.kazimad.reditparcer.interfaces.listeners.EndlessRecyclerViewScrollListener
import com.kazimad.reditparcer.models.inner_models.ChildItemWrapper
import com.kazimad.reditparcer.models.response.ChildrenItem
import com.kazimad.reditparcer.tools.Utils
import com.kazimad.reditparcer.view_models.ListResultFViewModel
import kotlinx.android.synthetic.main.fragment_list_result.*


class ListResultFragment : Fragment(), MainFragmentInterface, TopListAdapter.OnViewSelectedListener {
    companion object {
        const val MAX_ITEMS_COUNT: Int = 50
    }

    private lateinit var viewModel: ListResultFViewModel
    private lateinit var mainInterface: MainInterface
    private lateinit var loadMoreListener: EndlessRecyclerViewScrollListener

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_list_result, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        workOnRecycleView()
        viewModel = ViewModelProviders.of(this).get(ListResultFViewModel::class.java)
        viewModel.topLiveData.observe(this, Observer { onResultTopLiveData(it) })
        viewModel.errorLiveData.observe(this, Observer { onErrorLiveData(it) })
        viewModel.errorLiveData.value = null

        retryButton.setOnClickListener {
            loadingProgress.visibility = View.VISIBLE
            errorText.text = Utils.getResString(R.string.loading)
            retryButton.visibility = View.GONE
            viewModel.callListResults()
        }

        if (!viewModel.loadedChildrenItems.isEmpty()) {
            workWithDataForRecycleView(viewModel.loadedChildrenItems)
            topList.scrollToPosition(viewModel.lastPosition)
            viewModel.loadedChildrenItems.clear()
        } else {
            viewModel.callListResults()
        }
    }

    private fun onResultTopLiveData(modelData: ArrayList<ChildrenItem>?) {
        progressContainer.visibility = View.GONE
        workWithDataForRecycleView(modelData)
    }

    private fun onErrorLiveData(error: Throwable?) {
        mainInterface.onErrorCalled(error)
    }

    private fun workWithDataForRecycleView(modelData: ArrayList<ChildrenItem>?) {
        val toAdapterList: ArrayList<ChildItemWrapper> = ArrayList()
        if (modelData != null) {
            for (i in 0 until modelData.size) {
                toAdapterList.add(ChildItemWrapper(TopListAdapter.REGULAR_FLAG, modelData[i]))
            }
            (topList.adapter as TopListAdapter).addItems(toAdapterList)
            viewModel.topLiveData.postValue(null)
        }
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
    override fun onResume() {
        super.onResume()
        loadMoreListener.reset(0, true)
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

    override fun onDestroy() {
        super.onDestroy()
        viewModel.disposeAll()
    }

    private fun workOnRecycleView() {
        topList.layoutManager = LinearLayoutManager(topList.context)
        topList.adapter = TopListAdapter(this)
        loadMoreListener = object : EndlessRecyclerViewScrollListener(topList.layoutManager as LinearLayoutManager) {
            override fun onLoadMore() {
                if (topList.adapter.itemCount <= MAX_ITEMS_COUNT) {
                    viewModel.callListResults((topList.adapter as TopListAdapter).getItems()[(topList.adapter as TopListAdapter).getItems().lastIndex].childWrappedData?.data?.name)
                } else {
                    Toast.makeText(topList.context, Utils.getResString(R.string.limit_items), Toast.LENGTH_LONG).show()
                }
            }
        }
        topList.addOnScrollListener(loadMoreListener)
    }

    override fun onItemSelected(url: String?) {
        if (!url.isNullOrEmpty() && (url!!.toLowerCase().endsWith(".png")
                        || url.toLowerCase().endsWith(".jpeg") || url.toLowerCase().endsWith(".jpg")
                        || url.toLowerCase().endsWith(".bmp"))) {
            mainInterface.onAddToFragmentStackCalled(ImageFragment.newInstance(url))
        } else {
            Toast.makeText(topList.context, Utils.getResString(R.string.error_no_image), LENGTH_LONG).show()
        }
    }

    override fun onLoadError() {
        if (topList.adapter == null || topList.adapter.itemCount == 0) {
            retryButton.visibility = View.VISIBLE
        }

        loadingProgress.visibility = View.GONE
        errorText.text = Utils.getResString(R.string.error_connection_full)
        (topList.adapter as TopListAdapter).removeLoadingByForce()
        loadMoreListener.reset(0, true)

    }
}