package com.example.ajiekc.karoon.ui.newsfeed

import android.arch.lifecycle.Observer
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.ajiekc.karoon.LceState
import com.example.ajiekc.karoon.R
import com.example.ajiekc.karoon.extensions.toast
import com.example.ajiekc.karoon.ui.auth.AuthType
import com.example.ajiekc.karoon.ui.base.BaseFragment
import com.example.ajiekc.karoon.ui.main.MainActivity
import com.example.ajiekc.karoon.widget.BaseRecyclerAdapter
import com.example.ajiekc.karoon.widget.LceRecyclerView
import timber.log.Timber

class NewsfeedFragment : BaseFragment(), NewsfeedAdapter.RepeatButtonClickListener {

    companion object {
        val TAG = NewsfeedFragment::class.java.simpleName
    }

    private var mNextPageLoading = false
    private lateinit var mAdapter: NewsfeedAdapter
    private lateinit var mRecyclerView: LceRecyclerView
    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private val mViewModel by lazy(LazyThreadSafetyMode.NONE) {
        createViewModel<NewsfeedViewModel>()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as? MainActivity)?.setToolbarVisible(true)
        (activity as? MainActivity)?.setNavigationVisible(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_serach_users, container, false)
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout)
        setUpRefreshLayout()
        val linearLayoutManager = LinearLayoutManager(context)
        mRecyclerView = view.findViewById<LceRecyclerView>(R.id.users_recycler_view).apply {
            layoutManager = linearLayoutManager
            emptyView = view.findViewById(R.id.empty_text_view)
            progressView = view.findViewById(R.id.progress_view)
        }
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = linearLayoutManager.childCount
                val totalItemCount = linearLayoutManager.itemCount
                val pastVisibleItems =
                    linearLayoutManager.findFirstVisibleItemPosition()
                if (visibleItemCount + pastVisibleItems >= totalItemCount && isRecyclerScrollable(recyclerView) && !mNextPageLoading) {
                    val nextPage = mAdapter.getLastItem()?.nextFrom
                    nextPage?.let {
                        mViewModel.loadNews(startFrom = nextPage)
                    }
                }
            }
        })
        mAdapter = NewsfeedAdapter(mViewModel.dataSet)
        mAdapter.attachToRecyclerView(mRecyclerView)
        mAdapter.setOnRepeatButtonClickListener(this)
        mAdapter.setOnItemClickListener(object : BaseRecyclerAdapter.OnItemClickListener<NewsfeedItemViewModel> {
            override fun onItemClick(item: NewsfeedItemViewModel) {
                if (item.socialType == AuthType.GOOGLE.name && item.videoId.isNotEmpty()) {
                    mViewModel.navigateToVideo(item.videoId)
                }
            }
        })
        mViewModel.viewState().observe(this, Observer {
            renderViewState(it)
        })
        mViewModel.loadNews()

        return view
    }

    fun isRecyclerScrollable(recyclerView: RecyclerView): Boolean {
        return recyclerView.computeHorizontalScrollRange() > recyclerView.width
                || recyclerView.computeVerticalScrollRange() > recyclerView.height
    }

    private fun setUpRefreshLayout() {
        val color = ResourcesCompat.getColor(resources, R.color.colorAccent, null)
        mSwipeRefreshLayout.setColorSchemeColors(color)
        mSwipeRefreshLayout.setOnRefreshListener {
            mViewModel.loadNews(reload = true)
        }
    }

    private fun renderViewState(newsfeedState: NewsfeedViewState?) {
        when (newsfeedState?.state) {
            LceNewsfeedState.INITIAL_LOADING -> {
                mSwipeRefreshLayout.isEnabled = false
                mRecyclerView.showProgress()
                Log.d(TAG, "INITIAL_LOADING")
            }
            LceNewsfeedState.LOADING_NEXT_PAGE -> {
                mSwipeRefreshLayout.isEnabled = true
                mNextPageLoading = true
                if (mAdapter.isLastItemUser()) {
                    mAdapter.add(NewsfeedItemViewModel().apply { type = LceState.LOADING.name })
                } else {
                    mAdapter.replaceLastItem(NewsfeedItemViewModel().apply { type = LceState.LOADING.name })
                }
                Log.d(TAG, "LOADING_NEXT_PAGE")
            }
            LceNewsfeedState.NEXT_PAGE_LOADED -> {
                mSwipeRefreshLayout.isEnabled = true
                mNextPageLoading = false
                mAdapter.removeLastItem()
                Log.d(TAG, "NEXT_PAGE_LOADED")
            }
            LceNewsfeedState.LOADING -> {
                mSwipeRefreshLayout.isEnabled = true
                mSwipeRefreshLayout.isRefreshing = true
                Log.d(TAG, "LOADING")
            }
            LceNewsfeedState.HIDE_LOADING -> {
                mSwipeRefreshLayout.isEnabled = true
                mSwipeRefreshLayout.isRefreshing = false
                mRecyclerView.hideProgress()
                Log.d(TAG, "HIDE_LOADING")
            }
            LceNewsfeedState.CONTENT -> {
                mSwipeRefreshLayout.isEnabled = true
                mSwipeRefreshLayout.isRefreshing = false
                mRecyclerView.hideProgress()
                if (newsfeedState.reload) {
                    mNextPageLoading = false
                }
                onDataReceive(newsfeedState.data)
                Log.d(TAG, "CONTENT")
            }
            LceNewsfeedState.ERROR_NEXT_PAGE_LOADING -> {
                mSwipeRefreshLayout.isEnabled = true
                mAdapter.replaceLastItem(NewsfeedItemViewModel().apply { type = LceState.ERROR.name })
                Log.d(TAG, "ERROR_NEXT_PAGE_LOADING")
            }
            LceNewsfeedState.ERROR -> {
                mSwipeRefreshLayout.isEnabled = true
                mRecyclerView.updateView()
                context?.toast(getString(R.string.loading_failed))
                Log.d(TAG, "ERROR")
            }
        }
    }

    private fun onDataReceive(data: List<NewsfeedItemViewModel>?) {
        data?.let {
            mAdapter.changeDataSet(data)
        }
    }

    override fun onRepeatButtonClick() {
        val count = mAdapter.itemCount
        if (count > 1) {
            val nextPage = mAdapter.getItem(count - 2).nextFrom
            nextPage?.let {
                mViewModel.loadNews(startFrom = nextPage)
            }
        }
    }
}