package com.example.ajiekc.karoon.ui.newsfeed

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.ajiekc.karoon.repository.NewsfeedRepository
import com.example.ajiekc.karoon.ui.base.BaseViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsfeedViewModel @Inject constructor(
    private val repository: NewsfeedRepository
) : BaseViewModel() {

    private val viewState: MutableLiveData<NewsfeedViewState> = MutableLiveData()
    var dataSet = mutableListOf<NewsfeedItemViewModel>()

    fun viewState(): MutableLiveData<NewsfeedViewState> {
        return viewState
    }

    fun loadNews(reload: Boolean = false, startFrom: String? = null) {
        if (!reload && startFrom == null && !dataSet.isEmpty()) {
            return
        }
        loadData(reload, startFrom)
    }

    private fun loadData(reload: Boolean = false, startFrom: String? = null) {
        repository.loadNews(startFrom)
            .doOnSubscribe {
                viewState.postValue(
                    when {
                        startFrom != null -> NewsfeedViewState.loadingNextPage()
                        !reload -> NewsfeedViewState.initialLoading()
                        else -> NewsfeedViewState.loading()
                    }
                )
            }
            .doOnSuccess {
                if (startFrom != null) {
                    viewState.postValue(NewsfeedViewState.nextPageLoaded())
                } else {
                    viewState.postValue(NewsfeedViewState.hideLoading())
                }
            }
            .doOnError { viewState.postValue(NewsfeedViewState.hideLoading()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { list ->
                    if (reload) {
                        dataSet.clear()
                    }
                    dataSet.addAll(list)
                    viewState.value = NewsfeedViewState.content(dataSet, reload)
                },
                onError = { t: Throwable? ->
                    if (startFrom != null) {
                        viewState.postValue(NewsfeedViewState.errorNextPageLoading())
                    } else {
                        viewState.value = NewsfeedViewState.error(t)
                    }
                    Log.e("NewsfeedViewModel", "ERROR: ${t?.message}")
                }).disposeLater()
    }
}