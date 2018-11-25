package com.example.ajiekc.karoon.ui.newsfeed

import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.example.ajiekc.karoon.entity.VKNewsfeed
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
    var dataSet = mutableListOf<VKNewsfeed>()

    fun viewState(): MutableLiveData<NewsfeedViewState> {
        return viewState
    }

    fun loadNews() {
        repository.loadNews()
            .doOnSubscribe { viewState.postValue(NewsfeedViewState.loading()) }
            .doOnSuccess { viewState.postValue(NewsfeedViewState.hideLoading()) }
            .doOnError { viewState.postValue(NewsfeedViewState.hideLoading()) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onSuccess = { list ->
                    dataSet.addAll(list)
                    viewState.value = NewsfeedViewState.content(dataSet, false)
                },
                onError = { t: Throwable? ->
                    viewState.value = NewsfeedViewState.error(t)
                    Log.e("NewsfeedViewModel", "ERROR: ${t?.message}")
                }).disposeLater()
    }

//    fun loadData(reload: Boolean = false, since: Int = 0) {
//        if (!reload && since == 0 && !dataSet.isEmpty()) {
//            return
//        }
//        load(reload, since)
//    }

/*    private fun load(reload: Boolean = false, since: Int = 0) {
        disposables.add(repository.getUsers(since)
                .doOnSubscribe {
                    viewState.postValue(when {
                        since != 0 -> NewsfeedViewState.loadingNextPage()
                        !reload -> NewsfeedViewState.initialLoading()
                        else -> NewsfeedViewState.loading()
                    })
                }
                .doOnSuccess {
                    if (since != 0) {
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
                        if (since != 0) {
                            viewState.postValue(NewsfeedViewState.errorNextPageLoading())
                        } else {
                            viewState.value = NewsfeedViewState.error(t)
                        }
                        Log.e("NewsfeedViewModel", "ERROR: ${t?.message}")
                    }))
    }*/
}