package com.example.playlistmaker.domain.sharing

import com.example.playlistmaker.domain.sharing.model.SharingData

interface ExternalNavigator {

    fun shareLink()
    fun openEmail()
    fun openLink()

}