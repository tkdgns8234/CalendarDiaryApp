package com.hoon.calendardiaryapp.view.diary_view

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.core.net.toUri
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ActivityDiaryViewBinding
import com.hoon.calendardiaryapp.extensions.setImageWithGlide
import com.hoon.calendardiaryapp.extensions.toast
import com.hoon.calendardiaryapp.util.Constants.DATE_STRING_PATTERN
import com.hoon.calendardiaryapp.util.DateUtil
import com.hoon.calendardiaryapp.view.diary.DiaryActivity
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DiaryViewActivity : BaseActivity<DiaryViewViewModel, ActivityDiaryViewBinding>() {

    override fun getViewBinding() =
        ActivityDiaryViewBinding.inflate(layoutInflater)

    override val viewModel by viewModel<DiaryViewViewModel>()

    private val currentDate by lazy { intent.getSerializableExtra(INTENT_KEY_DATE) as Date }

    override fun observeData() {
        viewModel.diaryViewStateLiveData.observe(this) {
            when (it) {
                is DiaryViewState.UnInitialized -> {
                    initViews()
                    viewModel.fetchData(currentDate)
                }
                is DiaryViewState.UpdateDiaryContents -> {
                    handleUpdateDiaryContents(it)
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        setSupportActionBar(toolbar)

        toolbar.title = DateUtil.formatDate(currentDate, DATE_STRING_PATTERN)

        btnDelete.setOnClickListener { showDialogForDeleteEvent() }
        btnCorrect.setOnClickListener { startDiaryActivity(DiaryActivity.DiaryMode.MODIFY) }
    }

    private fun handleUpdateDiaryContents(state: DiaryViewState.UpdateDiaryContents) {
        when (state) {
            is DiaryViewState.UpdateDiaryContents.Success -> {
                val model = state.diaryModel
                with(binding) {
                    etTitle.setText(model.title)
                    etContent.setText(model.contents)

                    val imageURI = model.imageUri.toUri()
                    imageURI?.let {
                        ivDiaryImage.setImageWithGlide(this@DiaryViewActivity, it) {
                            binding.tvGallery.visibility = View.GONE
                            binding.ivGalleryIcon.visibility = View.GONE
                        }
                    }
                }
            }
            is DiaryViewState.UpdateDiaryContents.Fail -> {
                toast(resources.getString(R.string.failed_to_load_diary))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_diary, menu)
        return true
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

    private fun showDialogForDeleteEvent() {
        val msg = resources.getString(R.string.dialog_job_delete_msg)
        val deleteString = resources.getString(R.string.delete)
        val cancelString = resources.getString(R.string.cancel)

        AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton(deleteString) { _, _ ->
                viewModel.deleteDiaryInfo(currentDate)
                finish()
            }.setNegativeButton(cancelString) { _, _ -> }
            .create()
            .show()
    }

    private fun startDiaryActivity(mode: DiaryActivity.DiaryMode) {
        val intent = DiaryActivity.newIntent(this, mode, currentDate)
        startActivity(intent)
        finish()
    }

    companion object {
        const val INTENT_KEY_DATE = "Date"

        fun newIntent(context: Context, date: Date) =
            Intent(context, DiaryViewActivity::class.java).apply {
                putExtra(INTENT_KEY_DATE, date)
            }
    }
}