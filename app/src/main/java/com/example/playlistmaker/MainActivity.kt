package com.example.playlistmaker

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val searchIntent = Intent (this, SearchActivity::class.java)
        val searchButton = findViewById<Button>(R.id.buttonSearch)
        val searchButtonClickListener : View.OnClickListener = object : View.OnClickListener {
            override fun onClick(v: View?) {
                startActivity(searchIntent)
            }
        }
        searchButton.setOnClickListener(searchButtonClickListener)

        val libraryButton = findViewById<Button>(R.id.buttonLibrary)
        libraryButton.setOnClickListener {
            val libraryIntent = Intent (this, LibraryActivity::class.java)
            startActivity(libraryIntent)
        }

        val settingsButton = findViewById<Button>(R.id.buttonSettings)
        settingsButton.setOnClickListener {
            val settingsIntent = Intent (this, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}