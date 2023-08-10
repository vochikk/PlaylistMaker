package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

const val DARK_THEME_SETTINGS = "dark_theme_settings"
const val DARK_THEME_KEY = "dark_theme_key"

class App : Application() {
    private var darkTheme = false

    override fun onCreate () {
        super.onCreate()
        val sheredPref = getSharedPreferences(DARK_THEME_SETTINGS, MODE_PRIVATE)
        darkTheme = sheredPref.getBoolean(DARK_THEME_KEY, false)
        switchTheme(darkTheme)
    }

    fun switchTheme (darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}