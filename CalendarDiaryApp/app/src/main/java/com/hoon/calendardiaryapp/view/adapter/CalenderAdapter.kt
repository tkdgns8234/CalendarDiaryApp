package com.hoon.calendardiaryapp.view.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ItemCalenderBinding
import com.hoon.calendardiaryapp.extensions.*
import com.hoon.calendardiaryapp.util.CalendarManager
import java.util.*

class CalenderAdapter(
    private val updateCurrentMonth: (Calendar) -> Unit
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
    }

    override fun getItemCount() = calendarManager.days.count()

    fun prevMonth() {
        calendarManager.changeToPrevMonth {
            refreshView(it)
        }
        selectedDate = null
    }

    fun nextMonth() {
        calendarManager.changeToNextMonth {
            refreshView(it)
        }
        selectedDate = null
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
        updateCurrentMonth(calendar)
    }

    inner class ViewHolder(
        private val binding: ItemCalenderBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int) = with(binding) {
            val currentViewHolderDate: Date = calendarManager.days[position]

            val currentViewHolderMonth = currentViewHolderDate.getMonthString().toInt()
            val calendarMonth = calendarManager.calendar.get(Calendar.MONTH) + 1 // 1월 = 0

            val isSunday = position % CalendarManager.DAYS_OF_WEEK == 0
            val isSaturday = position % CalendarManager.DAYS_OF_WEEK == 6
            val isHoliday = holidays != null && holidays!!.contains(currentViewHolderDate.formatString())

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

            if (currentViewHolderDate.formatString() == selectedDate) {
                // 현재 클릭한 날짜 회색배경 처리
                ivSelectedEffect.alpha = 0F
                ivSelectedEffect.animate().alpha(1F).duration = 150
            } else {
                ivSelectedEffect.alpha = 0F
            }

            tvDay.text = currentViewHolderDate.getDayString()

            if (currentViewHolderMonth == calendarMonth) {
                root.setOnClickListener {
                    // 현재 클릭한 날짜 회색배경 처리
                    selectedDate = currentViewHolderDate.formatString()
                    refreshView(calendarManager.calendar)
                }
            }
        }
    }
}