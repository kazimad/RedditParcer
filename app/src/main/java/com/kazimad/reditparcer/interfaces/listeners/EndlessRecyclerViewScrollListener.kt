package com.kazimad.reditparcer.interfaces.listeners

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView


/**
 * Created by Kazimad on 26.05.2018.
 */
abstract class EndlessRecyclerViewScrollListener(private val layoutManager: LinearLayoutManager) : RecyclerView.OnScrollListener() {

    private var loading = true
    private var visibleThreshold = 0
    private var visibleItemCount = 0
    private var firstVisibleItem = 0
    private var previousTotal = 0
    private var totalItemCount = 0

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (dy > 0) {
            visibleItemCount = recyclerView.childCount
            totalItemCount = layoutManager.itemCount
            firstVisibleItem = layoutManager.findFirstVisibleItemPosition()

            if (loading) {
                if (totalItemCount > previousTotal) {
                    loading = false
                    previousTotal = totalItemCount
                }
            }
            if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
                // End has been reached
                onLoadMore()
                loading = true
            }
        }
    }

    fun reset(previousTotal: Int, loading: Boolean) {
        this.previousTotal = previousTotal
        this.loading = loading
    }

    abstract fun onLoadMore()
}