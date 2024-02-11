package com.example.playlistmaker.ui.settings.view_model

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.sharing.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor
): ViewModel() {

    private var themeLiveData = MutableLiveData(settingsInteractor.getThemeSettings())
    fun getLiveData(): LiveData<Boolean> = themeLiveData

    fun updateThemeSettings(isChecked: Boolean) {
        settingsInteractor.updateThemeSettings(isChecked)
        themeLiveData.value = isChecked
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

    companion object {
        fun getSettingsViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val sharingInteractor = Creator.providesSharingInteractor(context)
                val settingsInteractor = Creator.providesSettingsInteractor(context)

                SettingsViewModel(sharingInteractor, settingsInteractor)
            }
        }
    }


}