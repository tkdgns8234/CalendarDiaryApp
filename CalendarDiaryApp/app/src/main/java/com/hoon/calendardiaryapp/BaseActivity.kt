package com.hoon.calendardiaryapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding
import com.hoon.calendardiaryapp.util.LocaleHelper

abstract class BaseActivity<VM : BaseViewModel, VB : ViewBinding>(
    private val transitionMode: TransitionMode = TransitionMode.NONE
) : AppCompatActivity() {

    protected abstract val viewModel: VM

    protected lateinit var binding: VB

    abstract fun getViewBinding(): VB

    private lateinit var initialLocale: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        initState()
        openAnimate()
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

    override fun finish() {
        super.finish()
        closeAnimate()
    }

    private fun openAnimate() {
        when (transitionMode) {
            TransitionMode.HORIZONTAL -> overridePendingTransition(R.anim.horizon_enter, R.anim.none)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.vertical_enter, R.anim.none)
            TransitionMode.NONE -> Unit
        }
    }

    private fun closeAnimate() {
        when (transitionMode) {
            TransitionMode.HORIZONTAL -> overridePendingTransition(R.anim.none, R.anim.horizon_exit)
            TransitionMode.VERTICAL -> overridePendingTransition(R.anim.none, R.anim.vertical_exit)
            TransitionMode.NONE -> Unit
        }
    }

    enum class TransitionMode {
        NONE,
        HORIZONTAL,
        VERTICAL
    }
}