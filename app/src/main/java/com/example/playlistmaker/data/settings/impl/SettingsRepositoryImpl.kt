package com.example.playlistmaker.data.settings.impl

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.DARK_THEME_KEY
import com.example.playlistmaker.DARK_THEME_SETTINGS
import com.example.playlistmaker.data.settings.SettingsRepository

class SettingsRepositoryImpl(private val context: Context): SettingsRepository {

    private val sheredPref = context.getSharedPreferences(DARK_THEME_SETTINGS, AppCompatActivity.MODE_PRIVATE)

    override fun getThemeSettings(): Boolean {
        return sheredPref.getBoolean(DARK_THEME_KEY, false)
    }

    override fun updateThemeSettings(isChecked: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isChecked) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )

        sheredPref.edit()
            .putBoolean(DARK_THEME_KEY, isChecked)
            .apply()
    }

}