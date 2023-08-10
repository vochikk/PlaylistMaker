package com.example.playlistmaker

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.ImageView
import com.example.playlistmaker.databinding.ActivitySettingsBinding

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding =  ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonShare.setOnClickListener{
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT, "https://practicum.yandex.ru/profile/android-developer/")
            intent.type = "text/plain"
            startActivity(intent)
        }

        binding.buttonSupport.setOnClickListener{
            val message = getString(R.string.message)
            val subject = getString(R.string.subject)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("v.klimushin@ya.ru"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            shareIntent.data = Uri.parse("mailto:")
            startActivity(shareIntent)
        }

        binding.buttonTerms.setOnClickListener{
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://yandex.ru/legal/practicum_offer/")))
        }

        val sheredPref = getSharedPreferences(DARK_THEME_SETTINGS, MODE_PRIVATE)

        binding.themeSwitcher.isChecked = sheredPref.getBoolean(DARK_THEME_KEY, false)

        binding.themeSwitcher.setOnCheckedChangeListener { switcher, checked ->
            (applicationContext as App).switchTheme(checked)
            sheredPref.edit()
                .putBoolean(DARK_THEME_KEY, binding.themeSwitcher.isChecked)
                .apply()
        }



    }
}