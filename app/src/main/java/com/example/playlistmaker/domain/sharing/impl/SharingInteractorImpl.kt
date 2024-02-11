package com.example.playlistmaker.domain.sharing.impl

import android.content.Context
import com.example.playlistmaker.R
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.model.EmailData

class SharingInteractorImpl(
    private val externalNavigator: ExternalNavigator,
    private val context: Context
): SharingInteractor {

    override fun shareApp() {
        externalNavigator.shareLink(getShareLink())
    }

    override fun openTerms() {
        externalNavigator.openLink(getTermsLink())
    }

    override fun openSupport() {
        externalNavigator.openEmail(getSupportEmailData())
    }

    private fun getShareLink(): String {
        return context.getString(R.string.share)
    }

    private fun getSupportEmailData(): EmailData {
        return EmailData(
            message = context.getString(R.string.message),
            subject = context.getString(R.string.subject),
            emailAdress = context.getString(R.string.email_adress)
        )
    }

    private fun getTermsLink(): String {
        return context.getString(R.string.terms)
    }

}