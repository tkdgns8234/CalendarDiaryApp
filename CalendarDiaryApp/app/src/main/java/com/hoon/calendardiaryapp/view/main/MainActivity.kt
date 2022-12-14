package com.hoon.calendardiaryapp.view.main

import android.content.Context
import android.graphics.Point
import android.os.Build
import android.view.Menu
import android.view.MenuItem
import android.view.WindowManager
import androidx.core.net.toUri
import androidx.core.view.isVisible
import androidx.recyclerview.widget.GridLayoutManager
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.databinding.ActivityMainBinding
import com.hoon.calendardiaryapp.extensions.*
import com.hoon.calendardiaryapp.util.CalendarManager
import com.hoon.calendardiaryapp.util.DateUtil
import com.hoon.calendardiaryapp.view.adapter.CalenderAdapter
import com.hoon.calendardiaryapp.view.diary.DiaryActivity
import com.hoon.calendardiaryapp.view.diary_view.DiaryViewActivity
import com.hoon.calendardiaryapp.view.settings.SettingsActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class MainActivity : BaseActivity<MainViewModel, ActivityMainBinding>() {

    override val viewModel by viewModel<MainViewModel>()

    override fun getViewBinding() =
        ActivityMainBinding.inflate(layoutInflater)

    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var adapter: CalenderAdapter
    private var currentYear: String? = null
    private var currentMonth: String? = null

    override fun initViews() = with(binding) {
        setSupportActionBar(toolbar)

        adapter = CalenderAdapter(getViewHolderItemWidth(), onDateClickListener, updateUIListener)
        rvCalender.layoutManager =
            GridLayoutManager(this@MainActivity, CalendarManager.DAYS_OF_WEEK)
        rvCalender.adapter = adapter

        btnNextMonth.setOnClickListener {
            adapter.nextMonth()
        }

        btnPrevMonth.setOnClickListener {
            adapter.prevMonth()
        }

        layoutDiary.setOnClickListener {
            if (tvDiaryTitle.text.isNullOrEmpty().not()) {
                startDiaryViewActivity()
            }
        }

        btnRegisterDiary.setOnClickListener { startDiaryActivity(DiaryActivity.DiaryMode.NEW_REGISTER) }
    }

    override fun observeData() {
        viewModel.mainStateLiveData.observe(this) {
            when (it) {
                is MainState.GetHolidaysFromYear -> {
                    // Holidays update
                    handleGetHolidaysFromYear(it)
                }
                is MainState.GetDiaryContents -> {
                    // Main UI ????????? diary ?????? update
                    handleGetDiaryContentsState(it)
                }
                is MainState.UpdateDiaryWrittenList -> {
                    // ???????????? ???????????? ????????? ?????? ??? ??????
                    handleUpdateDiaryWrittenList(it.list)
                }
                is MainState.Loading -> {
                    handleLoadingState(it)
                }
                is MainState.Error -> {
                    handleErrorState(it.message)
                }
                else -> {}
            }
        }
    }

    override fun onResume() {
        adapter.getSelectedDate()?.let {
            // Main UI ????????? diary ?????? update
            viewModel.getDiaryContents(it)
            // ???????????? ???????????? ????????? ?????? ??? ??????
            viewModel.getDiaryContentsInMonth(it)
        }

        super.onResume()
    }

    private fun handleGetHolidaysFromYear(state: MainState.GetHolidaysFromYear) {
        when (state) {
            is MainState.GetHolidaysFromYear.Success -> {
                val holidayModels = state.list
                adapter.updateHolidays(holidayModels.map { it.date })
            }
            else -> {
                toast(resources.getString(R.string.failed_to_load_holidays))
            }
        }
    }

    private fun handleGetDiaryContentsState(state: MainState.GetDiaryContents) = with(binding) {
        when (state) {
            is MainState.GetDiaryContents.Success -> {
                val selectedDateContents = state.diaryModel

                tvDiaryTitle.text = selectedDateContents.title
                ivDiaryImage.setImageWithGlide(
                    this@MainActivity,
                    selectedDateContents.imageUri.toUri()
                ) {
                    btnRegisterDiary.visibility = false.toVisibility()
                }
            }
            is MainState.GetDiaryContents.Fail -> {
                tvDiaryTitle.text = null
                ivDiaryImage.setImageDrawable(null)

                val selectedDate = state.date
                val currentDate = Date(System.currentTimeMillis())

                val isBelowThanToday =
                    selectedDate.after(currentDate).not() // ?????? ???????????? ?????? ?????? ?????? true
                btnRegisterDiary.visibility = isBelowThanToday.toVisibility()
            }
        }
    }

    private fun handleUpdateDiaryWrittenList(list: List<DiaryModel>) {
        adapter.updateDiaryWrittenList(list)
    }

    private fun handleLoadingState(state: MainState.Loading) = with(binding) {
        when (state) {
            is MainState.Loading.Start -> progressBar.visibility = true.toVisibility()
            is MainState.Loading.End -> progressBar.visibility = false.toVisibility()
        }
    }

    private fun handleErrorState(message: String) {
        toast(message)
    }

    /**
     * calendar ?????? ??? ????????? ?????? ?????? ?????????
     */
    private val updateUIListener: (Date) -> Unit = { date ->
        with(binding) {
            val year = date.getYearString()
            val month = date.getMonthString()

            tvYear.text = year
            tvMonth.text = month

            if (currentYear != year) {
                currentYear = year
                // ?????? ????????? ?????? holiday list update
                viewModel.getHolidaysFromYear(date)
            }

            if (currentMonth != month) {
                currentMonth = month
                // ?????? ????????? ?????? ????????? ???????????? ?????? update
                viewModel.getDiaryContentsInMonth(date)
            }

            if (progressBar.isVisible) {
                progressBar.visibility = false.toVisibility()
            }
        }
    }

    /**
     * calendar ?????? ?????? ??? ?????????
     */
    private val onDateClickListener: (date: Date) -> Unit = { date ->
        val pattern = resources.getString(R.string.dateViewFormat)
        val dateString = DateUtil.dateToString(this, date, pattern)
        binding.tvSelectedDate.text = dateString

        // ????????? ??????????????? diary ?????? load
        viewModel.getDiaryContents(date)
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

    private fun startDiaryActivity(mode: DiaryActivity.DiaryMode) {
        val date = adapter.getSelectedDate()
        date?.let {
            val intent =
                DiaryActivity.newIntent(this@MainActivity, mode, date)
            startActivity(intent)
        }
    }

    private fun startDiaryViewActivity() {
        val date = adapter.getSelectedDate()
        date?.let {
            val intent =
                DiaryViewActivity.newIntent(this@MainActivity, date)
            startActivity(intent)
        }
    }

    private fun getViewHolderItemWidth(): Int {
        val widthPercent = resources.getString(R.string.device_width_percent).toDouble()
        return (getDeviceWidth() * widthPercent / CalendarManager.DAYS_OF_WEEK).toInt()
    }

    private fun getDeviceWidth(): Int {
        val windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager

        return if (Build.VERSION.SDK_INT >= 30) {
            windowManager.currentWindowMetrics.bounds.width()
        } else {
            val display = windowManager.defaultDisplay
            val size = Point()
            display.getSize(size)
            size.x
        }
    }
}