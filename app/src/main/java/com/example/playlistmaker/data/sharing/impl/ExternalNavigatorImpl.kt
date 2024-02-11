package com.example.playlistmaker.data.sharing.impl

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.playlistmaker.data.sharing.ExternalNavigator
import com.example.playlistmaker.domain.sharing.model.EmailData
import kotlin.coroutines.coroutineContext

class ExternalNavigatorImpl(private val context: Context): ExternalNavigator {

    private var intent = Intent()

    override fun shareLink(link: String) {
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_TEXT, link)
        intent.type = "text/plain"
        context.startActivity(intent)
    }

    override fun openEmail(emailData: EmailData) {
        intent = Intent(Intent.ACTION_SENDTO)
        intent.putExtra(Intent.EXTRA_EMAIL, emailData.emailAdress)
        intent.putExtra(Intent.EXTRA_SUBJECT, emailData.subject)
        intent.putExtra(Intent.EXTRA_TEXT, emailData.message)
        intent.data = Uri.parse("mailto:")
        context.startActivity(intent)
    }

    override fun openLink(link: String) {
        intent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
        context.startActivity(intent)
    }
}