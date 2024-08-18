package com.example.playlistmaker.domain.sharing

interface SharingInteractor {
    fun shareApp()
    fun openTerms()
    fun openSupport()
    fun shareTrack(str: String)
}