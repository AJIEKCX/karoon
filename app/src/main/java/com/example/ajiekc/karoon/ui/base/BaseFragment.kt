package com.example.ajiekc.karoon.ui.base

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v4.app.Fragment
import toothpick.Toothpick

abstract class BaseFragment : Fragment() {

    override fun onDestroy() {
        super.onDestroy()

        Toothpick.closeScope(this)
    }

    protected inline fun <reified T : ViewModel> createViewModel(): T {
        val factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val scope = Toothpick.openScopes(requireActivity().application, this@BaseFragment)

                return scope.getInstance(modelClass)
            }
        }

        return ViewModelProviders.of(this, factory).get(T::class.java)
    }
}