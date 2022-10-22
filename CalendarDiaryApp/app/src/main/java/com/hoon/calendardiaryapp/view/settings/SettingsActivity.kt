package com.hoon.calendardiaryapp.view.settings

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.hoon.calendardiaryapp.BaseActivity
import com.hoon.calendardiaryapp.BaseViewModel
import com.hoon.calendardiaryapp.R
import com.hoon.calendardiaryapp.databinding.ActivitySettingsBinding
import com.hoon.calendardiaryapp.util.LocaleHelper
import com.hoon.calendardiaryapp.util.PreferenceManager
import com.hoon.calendardiaryapp.view.main.MainViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class SettingsActivity : BaseActivity<BaseViewModel, ActivitySettingsBinding>(
    TransitionMode.HORIZONTAL
) {

    // this activity not used Viewmodel yet. (set BaseViewModel)
    override val viewModel by viewModel<BaseViewModel>()

    override fun getViewBinding() = ActivitySettingsBinding.inflate(layoutInflater)

    override fun observeData() {}

    companion object {
        const val TAG = "SettingsActivity"

        fun newIntent(context: Context) =
            Intent(context, SettingsActivity::class.java)
    }

    override fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        radioGroupLanguage.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radio_btn_korean -> {
                    updateLocaleInfo(LocaleHelper.LANGUAGE_KOREAN)
                }
                R.id.radio_btn_english -> {
                    updateLocaleInfo(LocaleHelper.LANGUAGE_ENGLISH)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        val locale = LocaleHelper.getPersistedLocale(this)

        when (locale) {
            LocaleHelper.LANGUAGE_KOREAN -> {
                binding.radioGroupLanguage.check(R.id.radio_btn_korean)
            }
            // 한국어 외의 다른 system language 는 english 로 설정
            else -> {
                binding.radioGroupLanguage.check(R.id.radio_btn_english)
            }
        }
    }

    private fun updateLocaleInfo(language: String) {
        val currentLocale = LocaleHelper.getPersistedLocale(this)

        if (language != currentLocale) {

            LocaleHelper.setLocale(this@SettingsActivity, language) {
                PreferenceManager(this).putCurrentLanguage(it)
            }
            recreate() // 언어 변경 즉시 적용
        }
    }
}