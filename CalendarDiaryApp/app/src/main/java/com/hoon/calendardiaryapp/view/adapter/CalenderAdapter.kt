package com.hoon.calendardiaryapp.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ItemCalenderBinding
import com.hoon.calendardiaryapp.extensions.getDayString
import com.hoon.calendardiaryapp.extensions.getMonthString
import com.hoon.calendardiaryapp.extensions.getYearString
import com.hoon.calendardiaryapp.extensions.setColor
import com.hoon.calendardiaryapp.util.CalendarManager
import com.hoon.calendardiaryapp.util.DateUtil
import java.util.*

class CalenderAdapter(
    private val onClickListener: (Date) -> Unit,
    private val updateUIListener: (Calendar) -> Unit
) : RecyclerView.Adapter<CalenderAdapter.ViewHolder>() {

    private val calendarManager = CalendarManager()
    private var selectedDate: String? = null
    private var holidays: List<String>? = null  // e.g) yyyy-mm-dd

    init {
        calendarManager.initCalendarManager { refreshView(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemCalenderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)

        val calendarMonth = calendarManager.calendar.get(Calendar.MONTH) + 1 // 1월 = 0
        val currentViewHolderMonth = calendarManager.days[position].getMonthString().toInt()

        if (calendarMonth == currentViewHolderMonth) {
            // 현재 표시되는 viewHolder 의 날짜가 이번 월인 경우 clickable
            holder.itemView.setOnClickListener {
                val date = calendarManager.days[position]
                onClickListener(date)

                selectedDate = DateUtil.formatDate(date, HOLIDAY_STRING_PATTERN) // 클릭한 날짜 회색배경 처리
                refreshView(calendarManager.calendar)
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
        refreshView(calendarManager.calendar) // update holidays and other will stay the same
    }

    /**
     *  변경된 calendar 값을 이용해 UI 업데이트
     *  ViewHolder class 의 bind() 호출로 이어짐
     */
    private fun refreshView(calendar: Calendar) {
        notifyDataSetChanged()
        updateUIListener(calendar)
    }

    fun updateSelectedDate() {
        val now = Date(System.currentTimeMillis())
        selectedDate = DateUtil.formatDate(now, HOLIDAY_STRING_PATTERN)

        val year = now.getYearString().toInt()
        val month = now.getMonthString().toInt()
        calendarManager.moveToTargetDate(year, month) {
            refreshView(it)
        }

        onClickListener(now) // call activity click listener
    }

    inner class ViewHolder(
        private val binding: ItemCalenderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) = with(binding) {
            val currentViewHolderDate: Date = calendarManager.days[position]
            val currentViewHolderDateString =
                DateUtil.formatDate(currentViewHolderDate, HOLIDAY_STRING_PATTERN)

            val currentViewHolderMonth = currentViewHolderDate.getMonthString().toInt()
            val calendarMonth = calendarManager.calendar.get(Calendar.MONTH) + 1 // 1월 = 0

            val isSunday = position % CalendarManager.DAYS_OF_WEEK == 0
            val isSaturday = position % CalendarManager.DAYS_OF_WEEK == 6
            val isHoliday =
                holidays != null && holidays!!.contains(currentViewHolderDateString)

            if (currentViewHolderMonth != calendarMonth) {  // 이전,다음 월 날짜인 경우
                tvDay.setColor(root.context, R.color.calender_date_gray)
            } else if (isSunday || isHoliday) {
                tvDay.setColor(root.context, R.color.calender_date_red)
            } else if (isSaturday) {
                tvDay.setColor(root.context, R.color.calender_date_blue)
            } else {
                tvDay.setColor(root.context, R.color.calender_date_gray)
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

            tvDay.text = currentViewHolderDate.getDayString()
        }
    }

    companion object {
        const val HOLIDAY_STRING_PATTERN = "yyyy-MM-dd"
    }
}