package com.hoon.calendardiaryapp.view.main

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.data.model.HolidayModel
import com.hoon.calendardiaryapp.databinding.ActivityMainBinding
import com.hoon.calendardiaryapp.extensions.toast
import com.hoon.calendardiaryapp.util.CalendarManager
import com.hoon.calendardiaryapp.util.DateUtil
import com.hoon.calendardiaryapp.view.adapter.CalenderAdapter
import com.hoon.calendardiaryapp.view.settings.SettingsActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override fun getViewBinding() =
        ActivityMainBinding.inflate(layoutInflater)

    override val viewModel by viewModel<MainViewModel>()

    private lateinit var adapter: CalenderAdapter
    private var currentYear: String? = null

    override fun observeData() {
        viewModel.mainStateLiveData.observe(this) {
            when (it) {
                is MainState.UnInitialized -> {
                    initViews()
                }
                is MainState.Success.GetHolidaysFromYear -> {
                    handleGetHolidaysFromYear(it.list)
                }
                is MainState.Loading -> {
                    handleLoadingState(it)
                }
                is MainState.Error -> {
                    handleErrorState(it.message)
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)

        adapter = CalenderAdapter(onClickListener, updateUIListener)
        rvCalender.layoutManager =
            GridLayoutManager(this@MainActivity, CalendarManager.DAYS_OF_WEEK)
        rvCalender.adapter = adapter

        btnNextMonth.setOnClickListener {
            adapter.nextMonth()
        }

        btnPrevMonth.setOnClickListener {
            adapter.prevMonth()
        }
    }

    private fun handleGetHolidaysFromYear(list: List<HolidayModel>) {
        adapter.updateHolidays(list.map { it.date })
    }

    private fun handleLoadingState(state: MainState.Loading) = with(binding) {
        when (state) {
            is MainState.Loading.Start -> progressBar.visibility = View.VISIBLE
            is MainState.Loading.End -> progressBar.visibility = View.GONE
        }
    }

    private fun handleErrorState(message: String) {
        toast(message)
    }

    private val updateUIListener: (Calendar) -> Unit = { calendar ->
        with(binding) {
            val sdfYear = SimpleDateFormat("yyyy")
            val sdfMonth = SimpleDateFormat("MM")
            val year = sdfYear.format(calendar.time)
            val month = sdfMonth.format(calendar.time)

            tvYear.text = year
            tvMonth.text = month

            if (currentYear != year) {
                currentYear = year
                // 해가 바뀌는 경우 holiday list update
                viewModel.getHolidaysFromYear(year)
            }
        }
    }

    private val onClickListener: (date: Date) -> Unit = { date ->
        with(binding) {
            val pattern = resources.getString(R.string.dateViewFormat)
            val dateString = DateUtil.formatDate(date, pattern)

            tvSelectedDate.text = dateString

            // TODO db에 현재 날짜에대한 데이터가 있으면 diaryView update
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        return when (item?.itemId) {
            R.id.menu_current_date -> {
                adapter.updateSelectedDate()
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