package com.example.ajiekc.karoon.ui.newsfeed

enum class LceNewsfeedState {
    INITIAL_LOADING,
    LOADING_NEXT_PAGE,
    NEXT_PAGE_LOADED,
    ERROR_NEXT_PAGE_LOADING,
    HIDE_LOADING,
    LOADING,
    CONTENT,
    ERROR
}