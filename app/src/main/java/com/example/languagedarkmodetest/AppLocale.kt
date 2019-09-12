package com.example.languagedarkmodetest

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import androidx.appcompat.view.ContextThemeWrapper
import androidx.preference.PreferenceManager
import org.apache.commons.lang3.LocaleUtils
import java.util.*

class AppLocale(private val localeProvider: UserLocaleProvider) {

    fun wrapContextWithAppLanguage(context: Context): Context {
        val userLocale: Locale = localeProvider.getCurrentStoreLanguage(context)
        Locale.setDefault(userLocale)

        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> updateResourcesLocale(
                context,
                userLocale
            )
            else -> updateResourcesLocaleLegacy(context, userLocale)
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResourcesLocale(context: Context, locale: Locale): Context {
        val configuration = context.resources.configuration
        configuration.setLocale(locale)
        return context.createConfigurationContext(configuration)
    }

    private fun updateResourcesLocaleLegacy(context: Context, locale: Locale): Context {
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        return context
    }
}

class UserLocaleProvider {

    fun getCurrentStoreLanguage(context: Context): Locale {
        val preferenceValue = PreferenceManager.getDefaultSharedPreferences(context)
            .getString(
                context.resources.getString(R.string.pref_language),
                context.resources.getString(R.string.pref_language_english)
            )!!
        return buildLocaleFromBCP47ToJavaFormat(preferenceValue)
    }

    /**
     * Transform language code from BCP47 tag string format (i.e. en-GB)
     * to a format complies with Java Locale String format (i.e. en_GB)
     *
     * @param bcp47Format the string to transform
     * @return a Java Locale.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun buildLocaleFromBCP47ToJavaFormat(bcp47Format: String?): Locale {
        return if (bcp47Format.isNullOrEmpty()) {
            Locale.getDefault()
        } else {
            LocaleUtils.toLocale(bcp47Format.replace("-", "_"))
        }
    }
}