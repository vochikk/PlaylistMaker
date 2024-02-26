package com.example.playlistmaker.ui.settings.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
): ViewModel() {

    private val _themeLiveData = MutableLiveData(settingsInteractor.getThemeSettings())
    val themeLiveData: LiveData<Boolean> = _themeLiveData

    fun updateThemeSettings(isChecked: Boolean) {
        settingsInteractor.updateThemeSettings(isChecked)
        _themeLiveData.value = isChecked
    }

    fun shareApp() {
        sharingInteractor.shareApp()
    }

    fun openTerms() {
        sharingInteractor.openTerms()
    }

    fun openSupport() {
        sharingInteractor.openSupport()
    }
}