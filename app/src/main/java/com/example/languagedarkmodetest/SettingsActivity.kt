package com.example.languagedarkmodetest

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat

class SettingsActivity : AppCompatActivity() {

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, SettingsActivity::class.java)
        }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(AppLocale(UserLocaleProvider()).wrapContextWithAppLanguage(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (supportFragmentManager.findFragmentById(android.R.id.content) == null) {
            supportFragmentManager.beginTransaction()
                .add(android.R.id.content, SettingsFragment()).commit()
        }
    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        }

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.preferences)

            findPreference<Preference>(resources.getString(R.string.pref_dark_mode))?.apply {
                onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, value ->

                        fun getPrefText(value: String): Int {
                            return when (value) {
                                "1" -> R.string.dark_mode_setting_never
                                "2" -> R.string.dark_mode_setting_always
                                else -> R.string.dark_mode_setting_follow_system
                            }
                        }

                        val string = value.toString()
                        val stringValue = resources.getString(getPrefText(string))
                        preference.summary = stringValue
                        AppCompatDelegate.setDefaultNightMode(Integer.parseInt(string))
                        true
                    }
            }

            findPreference<Preference>(resources.getString(R.string.pref_language))?.apply {
                onPreferenceChangeListener =
                    Preference.OnPreferenceChangeListener { preference, value ->

                        fun getPrefText(value: String): Int {
                            return when (value) {
                                "es" -> R.string.title_language_spanish
                                "fr" -> R.string.title_language_french
                                else -> R.string.title_language_english
                            }
                        }

                        val string = value.toString()
                        val stringValue = resources.getString(getPrefText(string))
                        preference.summary = stringValue
                        startActivity(MainActivity.restartIntent(context))

                        true
                    }
            }
        }
    }
}