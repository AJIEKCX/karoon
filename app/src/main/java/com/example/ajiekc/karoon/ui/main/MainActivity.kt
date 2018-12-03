package com.example.ajiekc.karoon.ui.main

import android.app.Activity
import android.content.Intent
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.view.View
import com.example.ajiekc.karoon.R
import com.example.ajiekc.karoon.databinding.ActivityMainBinding
import com.example.ajiekc.karoon.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.toolbar.*

class MainActivity : BaseActivity() {

    private val viewModel by lazy(LazyThreadSafetyMode.NONE) {
        createViewModel<MainViewModel>()
    }

    override val navigator by lazy(LazyThreadSafetyMode.NONE) {
        MainNavigator(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.vm = viewModel

        setSupportActionBar(toolbar)

        initEventListeners()
    }

    fun setNavigationVisible(flag: Boolean) {
        btvNavigation.visibility = if (flag) View.VISIBLE else View.GONE
    }

    fun setToolbarVisible(flag: Boolean) {
        toolbar.visibility = if (flag) View.VISIBLE else View.GONE
    }

    private fun initEventListeners() {
        btvNavigation.setOnNavigationItemSelectedListener {
            if (!it.isChecked) {
                when (it.itemId) {
                    R.id.item_home -> viewModel.navigateToNewsfeed()
                    R.id.item_profile -> viewModel.navigateToProfile()
                }
            }

            true
        }
    }

    companion object {
        fun start(activity: Activity) {
            val intent = Intent(activity, MainActivity::class.java)
            activity.startActivity(intent)
        }
    }
}
