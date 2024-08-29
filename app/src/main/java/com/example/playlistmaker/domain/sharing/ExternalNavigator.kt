package com.example.playlistmaker.domain.sharing


interface ExternalNavigator {

    fun shareLink()
    fun openEmail()
    fun openLink()
    fun shareTrack(str: String)
}