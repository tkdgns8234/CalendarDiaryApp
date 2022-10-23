package com.hoon.calendardiaryapp.view.main

import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.net.toUri
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

        adapter = CalenderAdapter(onDateClickListener, updateUIListener)
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
                    // Main UI 하단의 diary 내용 update
                    handleGetDiaryContentsState(it)
                }
                is MainState.UpdateDiaryWrittenList -> {
                    // 캘린더에 다이어리 작성된 날짜 점 찍기
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
            // Main UI 하단의 diary 내용 update
            viewModel.getDiaryContents(it)
            // 캘린더에 다이어리 작성된 날짜 점 찍기
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
                    selectedDate.after(currentDate).not() // 현재 날짜보다 크지 않은 경우 true
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
     * calendar 변경 및 초기화 되는 경우 호출됨
     */
    private val updateUIListener: (Date) -> Unit = { date ->
        with(binding) {
            val year = date.getYearString()
            val month = date.getMonthString()

            tvYear.text = year
            tvMonth.text = month

            if (currentYear != year) {
                currentYear = year
                // 해가 바뀌는 경우 holiday list update
                viewModel.getHolidaysFromYear(date)
            }

            if (currentMonth != month) {
                currentMonth = month
                // 월이 바뀌는 경우 일기가 쓰여있는 날짜 update
                viewModel.getDiaryContentsInMonth(date)
            }
        }
    }

    /**
     * calendar 날짜 클릭 시 호출됨
     */
    private val onDateClickListener: (date: Date) -> Unit = { date ->
        val pattern = resources.getString(R.string.dateViewFormat)
        val dateString = DateUtil.dateToString(this, date, pattern)
        binding.tvSelectedDate.text = dateString

        // 선택한 날짜에대한 diary 정보 load
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
}