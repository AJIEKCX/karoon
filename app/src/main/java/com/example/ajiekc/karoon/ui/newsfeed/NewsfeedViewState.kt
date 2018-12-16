package com.example.ajiekc.karoon.ui.newsfeed

data class NewsfeedViewState constructor(
    val state: LceNewsfeedState,
    val data: List<NewsfeedItemViewModel>? = null,
    val error: Throwable? = null,
    val reload: Boolean = false
) {
    companion object {
        fun initialLoading() = NewsfeedViewState(LceNewsfeedState.INITIAL_LOADING)
        fun loadingNextPage() = NewsfeedViewState(LceNewsfeedState.LOADING_NEXT_PAGE)
        fun loading() = NewsfeedViewState(LceNewsfeedState.LOADING)
        fun nextPageLoaded() = NewsfeedViewState(LceNewsfeedState.NEXT_PAGE_LOADED)
        fun hideLoading() = NewsfeedViewState(LceNewsfeedState.HIDE_LOADING)
        fun content(data: List<NewsfeedItemViewModel>?, reload: Boolean = false) = NewsfeedViewState(LceNewsfeedState.CONTENT, data, reload = reload)
        fun error(error: Throwable?) = NewsfeedViewState(LceNewsfeedState.ERROR, error = error)
        fun errorNextPageLoading() = NewsfeedViewState(LceNewsfeedState.ERROR_NEXT_PAGE_LOADING)
    }
}