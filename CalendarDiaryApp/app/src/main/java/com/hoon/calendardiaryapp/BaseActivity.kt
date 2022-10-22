package com.hoon.calendardiaryapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hoon.calendardiaryapp.util.LocaleHelper

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding> : AppCompatActivity() {

    protected abstract val viewModel: VM

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    private lateinit var initialLocale: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initState()
    }

    open fun initState() {
        initViews()
        observeData()
        initialLocale = LocaleHelper.getPersistedLocale(this)
    }

    open fun initViews() = Unit

    abstract fun observeData()

    override fun onResume() {
        super.onResume()

        if (initialLocale != LocaleHelper.LANGUAGE_SYSTEM
            && initialLocale != LocaleHelper.getPersistedLocale(this)
        ) {
            recreate()
        }
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(LocaleHelper.onAttach(base))
    }
}