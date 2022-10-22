package com.hoon.calendardiaryapp.view.diary

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.net.toUri
import androidx.core.widget.addTextChangedListener
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.data.model.DiaryModel
import com.hoon.calendardiaryapp.databinding.ActivityDiaryBinding
import com.hoon.calendardiaryapp.extensions.setImageWithGlide
import com.hoon.calendardiaryapp.extensions.toast
import com.hoon.calendardiaryapp.util.DateUtil
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*

class DiaryActivity : BaseActivity<DiaryViewModel, ActivityDiaryBinding>() {

    override val viewModel by viewModel<DiaryViewModel>()

    override fun getViewBinding() =
        ActivityDiaryBinding.inflate(layoutInflater)

    enum class DiaryMode {
        NEW_REGISTER,
        MODIFY
    }

    companion object {
        const val TAG = "DiaryActivity"
        const val INTENT_KEY_MODE = "Mode"
        const val INTENT_KEY_DATE = "Date"

        fun newIntent(context: Context, mode: DiaryMode, date: Date) =
            Intent(context, DiaryActivity::class.java).apply {
                putExtra(INTENT_KEY_MODE, mode)
                putExtra(INTENT_KEY_DATE, date)
            }
    }

    private val diaryMode by lazy { intent.getSerializableExtra(INTENT_KEY_MODE) as DiaryMode }
    private val currentDate by lazy { intent.getSerializableExtra(INTENT_KEY_DATE) as Date }
    private var imageURI: Uri? = null

    override fun initState() {
        super.initState()
        bindVies()
        viewModel.fetchData(currentDate)
    }

    override fun initViews() = with(binding) {
        when (diaryMode) {
            DiaryMode.MODIFY -> {
                toolbar.title = resources.getString(R.string.title_edit_diary)
                btnSave.text = resources.getString(R.string.edit)
            }

            DiaryMode.NEW_REGISTER -> {
                toolbar.title = resources.getString(R.string.title_register_new_diary)
                btnSave.text = resources.getString(R.string.save)
            }
        }

        val pattern = resources.getString(R.string.dateViewFormat)
        val dateString = DateUtil.formatDate(currentDate, pattern)
        tvCurrentDate.text = dateString

        setSupportActionBar(toolbar)
    }

    private fun bindVies() = with(binding) {
        ivDiaryImage.setOnClickListener { loadImage() }
        etTitle.addTextChangedListener { btnStateChange() }
        etContent.addTextChangedListener { btnStateChange() }
        btnSave.setOnClickListener {
            viewModel.saveDiaryDataInDB(
                DiaryModel(
                    date = currentDate,
                    title = etTitle.text.toString(),
                    imageUri = this@DiaryActivity.imageURI.toString(),
                    contents = etContent.text.toString()
                )
            )
            finish()
        }
    }

    override fun observeData() {
        viewModel.diaryStateLiveData.observe(this) {
            when (it) {
                is DiaryState.Success.FetchData -> {
                    handleSuccessFetchData(it.diaryModel)
                }
                is DiaryState.Loading -> {
                    handleLoadingState(it)
                }
                else -> {}
            }
        }
    }

    private fun handleSuccessFetchData(diaryModel: DiaryModel) = with(binding) {
        tvCurrentDate.text =
            DateUtil.formatDate(diaryModel.date, resources.getString(R.string.dateViewFormat))
        etTitle.setText(diaryModel.title)
        etContent.setText(diaryModel.contents)

        imageURI = diaryModel.imageUri.toUri()
        imageURI?.let {
            ivDiaryImage.setImageWithGlide(this@DiaryActivity, it) {
                binding.tvGallery.visibility = View.GONE
                binding.ivGalleryIcon.visibility = View.GONE
            }
        }
    }

    private fun handleLoadingState(state: DiaryState.Loading) = with(binding) {
        when (state) {
            is DiaryState.Loading.Start -> progressBar.visibility = View.VISIBLE
            is DiaryState.Loading.End -> progressBar.visibility = View.GONE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_toolbar_diary, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item?.itemId) {
            R.id.menu_close -> {
                showDialogForCloseEvent()
                true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun loadImage() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "image/*"
        galleryImageLauncher.launch(intent)
    }

    private val galleryImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                imageURI = it.data?.data
                imageURI?.let {
                    contentResolver.takePersistableUriPermission(it, Intent.FLAG_GRANT_READ_URI_PERMISSION)

                    binding.ivDiaryImage.setImageWithGlide(this, it) {
                        btnStateChange()

                        binding.tvGallery.visibility = View.GONE
                        binding.ivGalleryIcon.visibility = View.GONE
                    }
                }

            } else if (it.resultCode == RESULT_CANCELED) {
                toast(resources.getString(R.string.deselected_photo))
            } else {
                Log.d(TAG, "something wrong with gallery launcher")
            }
        }

    private fun btnStateChange() = with(binding) {
        btnSave.isEnabled =
            !etTitle.text.isNullOrEmpty() &&
                    !etContent.text.isNullOrEmpty() &&
                    imageURI != null
    }

    private fun showDialogForCloseEvent() {
        val msg = resources.getString(R.string.dialog_job_cancel_msg)
        val no = resources.getString(R.string.no)
        val yes = resources.getString(R.string.yes)

        AlertDialog.Builder(this)
            .setMessage(msg)
            .setPositiveButton(yes) { _, _ ->
                finish()
            }.setNegativeButton(no) { _, _ -> }
            .create()
            .show()
    }
}