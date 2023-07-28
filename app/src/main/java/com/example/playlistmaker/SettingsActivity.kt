package com.example.playlistmaker

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val backButton = findViewById<ImageView>(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }

        val buttonShare = findViewById<FrameLayout>(R.id.buttonShare)
        buttonShare.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer/")
            intent.type = "text/plain"
            startActivity(intent)
        }

        val buttonSupport = findViewById<FrameLayout>(R.id.buttonSupport)
        buttonSupport.setOnClickListener{
            val message = getString(R.string.message)
            val subject = getString(R.string.subject)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("v.klimushin@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.data = Uri.parse("mailto:")
            startActivity(shareIntent)
        }

        val buttonTerms = findViewById<FrameLayout>(R.id.buttonTerms)
        buttonTerms.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/")))
        }

    }
}