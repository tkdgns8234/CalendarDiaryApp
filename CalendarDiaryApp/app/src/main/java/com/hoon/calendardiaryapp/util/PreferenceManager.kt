package com.hoon.calendardiaryapp.util

import android.content.Context
import android.content.SharedPreferences

class PreferenceManager(
    private val context: Context
) {
    companion object {
        const val PREFERENCE_NAME = "com-hoon-calendardiaryapp-pref"
        const val KEY_CURRENT_LANGUAGE = "LANGUAGE"
    }

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private val pref by lazy { getSharedPreference() }

    private val editor by lazy { pref.edit() }

    fun putCurrentLanguage(language: String) {
        editor.putString(KEY_CURRENT_LANGUAGE, language)
        editor.apply()
    }

    /**
     * 현재 설정된 language 가 없는경우 system language 로 설정
     */
    fun getCurrentLanguage(): String {
        return pref.getString(KEY_CURRENT_LANGUAGE, LocaleHelper.LANGUAGE_SYSTEM)!!
    }
}