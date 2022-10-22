package com.hoon.calendardiaryapp.util

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Resources
import android.os.Build
import java.util.*

/**
 * 다국어 관련 참조
 * https://developer.android.com/reference/java/util/Locale
 * https://stackoverflow.com/questions/4985805/set-locale-programmatically#comment106949548_44571077
 */

object LocaleHelper {

    const val LANGUAGE_ENGLISH = "en"
    const val LANGUAGE_KOREAN = "ko"
    const val LANGUAGE_SYSTEM = "system"

    fun onAttach(context: Context): Context {
        val locale = getPersistedLocale(context)
        return setLocale(context, locale, null)
    }

    fun getPersistedLocale(context: Context): String {
        return return PreferenceManager(context).getCurrentLanguage()
    }

    /**
     * Set the app's locale to the one specified by the given String.
     */
    fun setLocale(context: Context, localeSpec: String?, action: ((language: String) -> Unit)?): Context {
        var locale: Locale = if (localeSpec == LANGUAGE_SYSTEM) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Resources.getSystem().configuration.locales[0]
            } else {
                Resources.getSystem().configuration.locale
            }
        } else {
            Locale(localeSpec)
        }

        // 한국어, 영어 외의 언어는 English 로 설정
        if (locale.language != LANGUAGE_KOREAN && locale.language != LANGUAGE_ENGLISH) {
            locale = Locale(LANGUAGE_ENGLISH)
        }

        action?.let {
            it(locale.language)
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, locale)
        } else {
            updateResourcesLegacy(context, locale)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        configuration.setLayoutDirection(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.locale = locale
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            configuration.setLayoutDirection(locale)
        }
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}