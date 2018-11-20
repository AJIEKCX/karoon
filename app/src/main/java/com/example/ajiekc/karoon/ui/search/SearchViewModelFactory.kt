package com.example.ajiekc.karoon.ui.search

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import com.example.ajiekc.karoon.repository.IGithubRepository

class SearchViewModelFactory(private val githubRepo: IGithubRepository)
    : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            return SearchViewModel(githubRepo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}