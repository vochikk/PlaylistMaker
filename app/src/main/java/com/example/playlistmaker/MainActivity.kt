package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.example.playlistmaker.databinding.ActivityMainBinding

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonSearch.setOnClickListener {
            startActivity(Intent (this, SearchActivity::class.java))
        }

        binding.buttonLibrary.setOnClickListener {
            startActivity(Intent (this, LibraryActivity::class.java))
        }

        binding.buttonSettings.setOnClickListener {
            startActivity(Intent (this, SettingsActivity::class.java))
        }
    }
}