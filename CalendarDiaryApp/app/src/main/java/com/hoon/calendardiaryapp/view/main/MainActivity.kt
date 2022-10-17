package com.hoon.calendardiaryapp.view.main

import android.view.MenuItem
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ActivityMainBinding
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel by viewModel<MainViewModel>()

    override fun getViewBinding() =
        ActivityMainBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.mainStateLiveData.observe(this) {
            when (it) {
                MainState.UnInitialized -> {
                    initViews()
                }
                else -> {}
            }
        }
    }

    private fun initViews() = with(binding) {

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item?.itemId){
            R.id.menu_current_date -> {
                true
            }
            R.id.menu_settings -> {
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}