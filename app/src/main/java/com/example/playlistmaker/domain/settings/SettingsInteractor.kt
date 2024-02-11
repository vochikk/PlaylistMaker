package com.example.playlistmaker.domain.settings


interface SettingsInteractor {
    fun getThemeSettings(): Boolean
    fun updateThemeSettings(isChecked: Boolean)
}