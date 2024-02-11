package com.example.playlistmaker.domain.settings.impl

import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.domain.settings.SettingsInteractor

class SettingsInteractorImpl(private val settingsRepository: SettingsRepository): SettingsInteractor {

    override fun getThemeSettings(): Boolean {
        return settingsRepository.getThemeSettings()
    }

    override fun updateThemeSettings(isCheked: Boolean) {
        settingsRepository.updateThemeSettings(isCheked)
    }
}