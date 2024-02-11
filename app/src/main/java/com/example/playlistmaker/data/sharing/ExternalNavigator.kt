package com.example.playlistmaker.data.sharing

import com.example.playlistmaker.domain.sharing.model.EmailData

interface ExternalNavigator {

    fun shareLink(link: String)
    fun openEmail(emailData: EmailData)
    fun openLink(link: String)

}