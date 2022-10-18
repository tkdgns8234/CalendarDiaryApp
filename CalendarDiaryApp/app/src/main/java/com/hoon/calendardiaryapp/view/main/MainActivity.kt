package com.hoon.calendardiaryapp.view.main

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ActivityMainBinding
import com.hoon.calendardiaryapp.view.settings.SettingsActivity
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
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.e(TAG, "onOptionsItemSelected()")

        return when (item?.itemId) {
            R.id.menu_current_date -> {
                true
            }
            R.id.menu_settings -> {
                startActivity(SettingsActivity.newIntent(this))
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        const val TAG = "MainActivity"
    }
}