package com.example.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.creator.Creator

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val interactor = Creator.providesSettingsInteractor(this)

        var isThemeDark = interactor.getThemeSettings()

        interactor.updateThemeSettings(isThemeDark)
    }
}