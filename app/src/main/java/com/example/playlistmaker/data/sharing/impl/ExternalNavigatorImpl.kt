package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.SharingData

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    private var intent = Intent()

    private val sharingData = SharingData(
        link = context.getString(R.string.share),
        termLink = context.getString(R.string.terms),
        emailAdress = context.getString(R.string.email_adress),
        subject = context.getString(R.string.subject),
        message = context.getString(R.string.message)
    )

    override fun shareLink() {
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, sharingData.link)
        intent.type = "text/plain"
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openEmail() {
        intent = Intent(Intent.ACTION_SENDTO)
        intent.putExtra(Intent.EXTRA_EMAIL, sharingData.emailAdress)
        intent.putExtra(Intent.EXTRA_SUBJECT, sharingData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, sharingData.message)
        intent.data = Uri.parse("mailto:")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    override fun openLink() {
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(sharingData.termLink))
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }
}