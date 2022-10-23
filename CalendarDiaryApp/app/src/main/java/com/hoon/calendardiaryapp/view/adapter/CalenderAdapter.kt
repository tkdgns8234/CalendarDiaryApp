package com.hoon.calendardiaryapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.databinding.ItemCalenderBinding
import com.hoon.calendardiaryapp.extensions.*
import com.hoon.calendardiaryapp.util.CalendarManager
import com.hoon.calendardiaryapp.util.Constants.DATE_STRING_PATTERN
import com.hoon.calendardiaryapp.util.DateUtil
import java.util.*

class CalenderAdapter(
    private val itemWidth: Int,
    private val onDateClickCallback: (Date) -> Unit,
    private val updateUICallback: (Date) -> Unit
) : RecyclerView.Adapter<CalenderAdapter.ViewHolder>() {

    private val calendarManager = CalendarManager()
    private var selectedDate: String? = null // user 가 선택한 날짜
    private var holidays: List<String>? = null  // e.g) yyyy-mm-dd
    private var diaryWrittenDateList: List<String>? = null // 일기가 쓰여있는 날짜

    init {
        calendarManager.initCalendarManager { refreshView(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ItemCalenderBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )

        val params = view.root.layoutParams
        params.height = itemWidth
        params.width = itemWidth
        view.root.layoutParams = params

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        val calendarMonth = calendarManager.calendar.get(Calendar.MONTH) + 1 // 1월 = 0
        val currentViewHolderMonth = calendarManager.days[position].getMonthString().toInt()

        if (calendarMonth == currentViewHolderMonth) {
            // 현재 표시되는 viewHolder 의 날짜가 이번 월인 경우 clickable
            holder.itemView.setOnClickListener {
                val date = calendarManager.days[position]
                selectedDate = DateUtil.dateToString(date, DATE_STRING_PATTERN) // 클릭한 날짜 회색배경 처리

                refreshView(calendarManager.calendar)
                onDateClickCallback(date)
            }
        } else {
            // 간혹 기존 리스너가 제거되지 않는 경우가 있어 처리
            holder.itemView.setOnClickListener { null }
        }
    }

    override fun getItemCount() = calendarManager.days.count()

    fun prevMonth() {
        calendarManager.changeToPrevMonth {
            refreshView(it)
        }
    }

    fun nextMonth() {
        calendarManager.changeToNextMonth {
            refreshView(it)
        }
    }

    fun updateHolidays(holidays: List<String>) {
        this.holidays = holidays
        refreshView(calendarManager.calendar)
    }

    /**
     * 캘린더에 다이어리 작성된 날짜 점 찍기
     * @param list : 현재 Month에 해당하는 다이어리 작성된 날짜 list
     */
    fun updateDiaryWrittenList(list: List<DiaryModel>) {
        this.diaryWrittenDateList = list.map {
            DateUtil.dateToString(it.date, DATE_STRING_PATTERN)
        }
        refreshView(calendarManager.calendar)
    }

    fun updateSelectedDate() {
        val now = Date(System.currentTimeMillis())
        selectedDate = DateUtil.dateToString(now, DATE_STRING_PATTERN)

        val year = now.getYearString().toInt()
        val month = now.getMonthString().toInt()
        calendarManager.moveToTargetDate(year, month) {
            refreshView(it)
        }

        onDateClickCallback(now) // call activity click listener
    }

    fun getSelectedDate(): Date? {
        return selectedDate?.run {
            DateUtil.stringToDate(this, DATE_STRING_PATTERN)
        }
    }

    /**
     *  변경된 calendar 값을 이용해 UI 업데이트
     *  ViewHolder class 의 bind() 호출로 이어짐
     */
    private fun refreshView(calendar: Calendar) {
        notifyDataSetChanged()
        updateUICallback(calendar.time)
    }

    inner class ViewHolder(
        private val binding: ItemCalenderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) = with(binding) {
            val currentViewHolderDate: Date = calendarManager.days[position]
            val currentViewHolderDateString =
                DateUtil.dateToString(currentViewHolderDate, DATE_STRING_PATTERN)

            val currentViewHolderMonth = currentViewHolderDate.getMonthString().toInt()
            val calendarMonth = calendarManager.calendar.get(Calendar.MONTH) + 1 // 1월 = 0

            val isSunday = position % CalendarManager.DAYS_OF_WEEK == 0
            val isSaturday = position % CalendarManager.DAYS_OF_WEEK == 6
            val isHoliday =
                holidays != null && holidays!!.contains(currentViewHolderDateString)

            if (currentViewHolderMonth != calendarMonth) {  // 이전,다음 월 날짜인 경우 토, 일, 공휴일 상관없이 회색 처리
                tvDay.setColor(root.context, R.color.calender_date_white)
            } else if (isSunday || isHoliday) {
                tvDay.setColor(root.context, R.color.calender_date_red)
            } else if (isSaturday) {
                tvDay.setColor(root.context, R.color.calender_date_blue)
            } else {
                tvDay.setColor(root.context, R.color.calender_date_white)
            }

            if (currentViewHolderMonth == calendarMonth) {
                tvDay.alpha = 1F
            } else {
                tvDay.alpha = 0.35F // 이전,다음 월 날짜인 경우
            }

            if (currentViewHolderDateString == selectedDate) {
                // 현재 클릭한 날짜 회색배경 처리
                ivSelectedEffect.alpha = 0F
                ivSelectedEffect.animate().alpha(1F).duration = 150
            } else {
                ivSelectedEffect.alpha = 0F
            }

            diaryWrittenDateList?.let {
                // 다이어리가 작성된 날짜인 경우 점 추가
                val isWrittenDiaryDate = it.contains(currentViewHolderDateString)
                ivDiaryDot.visibility = isWrittenDiaryDate.toVisibility()
            }

            tvDay.text = currentViewHolderDate.getDayString()
        }
    }
}