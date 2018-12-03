package com.example.ajiekc.karoon.ui

import android.databinding.BindingAdapter
import android.support.design.widget.TextInputLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import com.example.ajiekc.karoon.R
import com.example.ajiekc.karoon.extensions.loadRoundedImage
import com.squareup.picasso.Picasso

@BindingAdapter("isGone")
fun setIsGone(view: View, value: Boolean) {
    view.visibility = if (value) View.GONE else View.VISIBLE
}

@BindingAdapter("isHidden")
fun setIsHidden(view: View, value: Boolean) {
    view.visibility = if (value) View.INVISIBLE else View.VISIBLE
}

@BindingAdapter("isVisible")
fun setIsVisible(view: View, value: Boolean) {
    view.visibility = if (value) View.VISIBLE else View.GONE
}

@BindingAdapter("isEnabled")
fun setIsEnabled(view: Button, value: Boolean) {
    view.isEnabled = value
    view.alpha = if (value) 1.0f else 0.6f
}

@BindingAdapter("isEnabled")
fun setIsEnabled(view: SwipeRefreshLayout, value: Boolean) {
    view.isEnabled = value
}

@BindingAdapter("isRefreshing")
fun setIsRefreshing(view: SwipeRefreshLayout, value: Boolean) {
    view.isRefreshing = value
}

@BindingAdapter("errorText")
fun setErrorText(view: TextInputLayout, value: String?) {
    view.error = value
}

@BindingAdapter("isNestedScrollEnabled")
fun setIsNestedScrollEnabled(view: RecyclerView, value: Boolean) {
    view.isNestedScrollingEnabled = value
}

@BindingAdapter("hasFixedSize")
fun setHasFixedSize(view: RecyclerView, value: Boolean) {
    view.setHasFixedSize(value)
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, imageUrl: String?) {
    if (imageUrl == null || imageUrl.isNotEmpty()) {
        Picasso.get()
            .loadRoundedImage(imageUrl, view, R.drawable.ic_user_placeholder)
    }
}