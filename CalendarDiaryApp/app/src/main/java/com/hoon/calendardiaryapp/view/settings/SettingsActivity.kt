package com.hoon.calendardiaryapp.view.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.hoon.calendardiaryapp.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    val binding = ActivitySettingsBinding.inflate(layoutInflater)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
    }

    private fun initViews() = with(binding) {
        toolbar.setNavigationOnClickListener {
            finish()
        }

        /* TODO: radio group 처리 추가, default value:디바이스 설정을 따라감
         * 설정 값은 저장되어야하고 즉시 번역되어야함 (sharedPref 등에 저장하자)
         */
    }
}