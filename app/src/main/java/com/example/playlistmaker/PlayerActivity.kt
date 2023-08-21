package com.example.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.google.gson.Gson

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val json = intent.getStringExtra(TRACK_KEY)
        val track = Gson().fromJson(json, Track::class.java)
        drawLayout(track)

        binding.buttonBack.setOnClickListener {
            finish()
        }
    }

    private fun drawLayout (track: Track) {

        binding.trackName.setText(track.trackName)
        binding.artistName.setText(track.artistName)
        binding.trackTime.setText(track.formatTime(track.trackTimeMillis))
        binding.releaseDate.setText(track.getReleaseYear())
        binding.genreName.setText(track.primaryGenreName)
        binding.country.setText(track.country)

        val itemUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(itemUrl)
            .placeholder(R.drawable.ic_placeholder_312x312)
            .transform(RoundedCorners(24))
            .into(binding.imageAlbum)

        if (track.collectionName?.isEmpty() == null) {
            binding.albumGroup.visibility = View.GONE
        } else {
            binding.albumName.setText(track.collectionName)
        }
    }

}