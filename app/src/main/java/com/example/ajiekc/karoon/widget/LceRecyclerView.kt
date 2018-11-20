package com.example.ajiekc.karoon.widget

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import com.example.ajiekc.karoon.extensions.hide
import com.example.ajiekc.karoon.extensions.show

class LceRecyclerView(context: Context, attrs: AttributeSet) : RecyclerView(context, attrs) {

    var emptyView: View? = null
    var progressView: View? = null

    fun showProgress() {
        progressView?.show()
        emptyView?.hide()
        this.hide()
    }

    fun hideProgress() {
        progressView?.hide()
    }

    fun updateView() {
        if (adapter!!.itemCount > 0) {
            showContent()
        } else {
            showEmptyView()
        }
    }

    private fun showEmptyView() {
        emptyView?.show()
        this.hide()

    }

    private fun showContent() {
        emptyView?.hide()
        this.show()
    }
}