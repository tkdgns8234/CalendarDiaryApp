package com.hoon.calendardiaryapp.view.diary_view

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ActivityDiaryViewBinding
import com.hoon.calendardiaryapp.view.diary.DiaryActivity
import org.koin.android.viewmodel.ext.android.viewModel

class DiaryViewActivity : BaseActivity<DiaryViewViewModel, ActivityDiaryViewBinding>() {

    override val viewModel by viewModel<DiaryViewViewModel>()

    override fun getViewBinding() =
        ActivityDiaryViewBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.diaryViewStateLiveData.observe(this) {
            when (it) {
                DiaryViewState.UnInitialized -> {
                    initViews()
                }
                else -> {}
            }
        }
    }

    private fun initViews() = with(binding) {
        val date = intent.getStringExtra(INTENT_KEY_DATE)
        toolbar.title = date
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.menu_close -> {
                finish()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    companion object {
        const val INTENT_KEY_DATE = "Date"

        fun newIntent(context: Context, date: String) =
            Intent(context, DiaryActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, date)
            }
    }
}