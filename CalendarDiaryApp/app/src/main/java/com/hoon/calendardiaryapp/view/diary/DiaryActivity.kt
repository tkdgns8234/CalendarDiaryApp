package com.hoon.calendardiaryapp.view.diary

import android.content.Context
import android.content.Intent
import android.view.MenuItem
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ActivityDiaryBinding
import org.koin.android.viewmodel.ext.android.viewModel

class DiaryActivity : BaseActivity<DiaryViewModel, ActivityDiaryBinding>() {

    private val diaryMode by lazy { intent.getSerializableExtra(INTENT_KEY_DIARY_MODE) as DiaryMode }

    override val viewModel by viewModel<DiaryViewModel>()

    override fun getViewBinding() =
        ActivityDiaryBinding.inflate(layoutInflater)

    override fun observeData() {
        viewModel.diaryStateLiveData.observe(this) {
            when (it) {
                DiaryState.UnInitialized -> {
                    initViews()
                }
                else -> {}
            }
        }
    }

    private fun initViews() = with(binding) {

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
        const val INTENT_KEY_DIARY_MODE = "DiaryMode"

        fun newIntent(context: Context, mode: DiaryMode) =
            Intent(context, DiaryActivity::class.java).apply {
                putExtra(INTENT_KEY_DIARY_MODE, mode)
            }
    }

    enum class DiaryMode {
        NEW_REGISTER,
        MODIFY
    }
}