package com.example.playlistmaker.ui.player.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityPlayerBinding
import com.example.playlistmaker.domain.player.state.PlayerState
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment.Companion.TRACK_KEY
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    private var handler = Handler(Looper.getMainLooper())
    private val viewModel by viewModel<PlayerViewModel>()
    private var newPlayerState = PlayerState.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        viewModel.playerStateLiveData.observe(this){state ->
            render(state)
        }

        val json = intent.getStringExtra(TRACK_KEY)
        val track = Gson().fromJson(json, Track::class.java)

        drawLayout(track)
        viewModel.prepare(track)

        binding.buttonBack.setOnClickListener {
            this.onBackPressedDispatcher.onBackPressed()
        }

        binding.buttonPlay.setOnClickListener {
            if (newPlayerState == PlayerState.PLAYING) {
                viewModel.pause()
            } else {
                viewModel.play()
                startTimer()
            }
        }

    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
        handler.removeCallbacksAndMessages(null)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.realese()
        handler.removeCallbacksAndMessages(null)
    }

    private fun render (state: PlayerState) {
        when (state) {
            PlayerState.DEFAULT -> {}
            PlayerState.PREPARED -> {
                binding.buttonPlay.setImageResource(R.drawable.ic_button_play)
            }
            PlayerState.PLAYING -> {
                binding.buttonPlay.setImageResource(R.drawable.ic_button_pause)
            }
            PlayerState.PAUSING -> {
                binding.buttonPlay.setImageResource(R.drawable.ic_button_play)
            }
            PlayerState.ENDING -> {
                handler.removeCallbacksAndMessages(null)
                binding.playTime.text = "00:00"
                binding.buttonPlay.setImageResource(R.drawable.ic_button_play)
            }
        }
        newPlayerState = state
    }

    private fun drawLayout (track: Track) {

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.formatTime(track.trackTimeMillis)
        binding.releaseDate.text = track.getReleaseYear()
        binding.genreName.text = track.primaryGenreName
        binding.country.text = track.country

        val itemUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(itemUrl)
            .placeholder(R.drawable.ic_placeholder_312x312)
            .transform(RoundedCorners(24))
            .into(binding.imageAlbum)

        if (track.collectionName?.isEmpty() == null) {
            binding.albumGroup.visibility = View.GONE
        } else {
            binding.albumName.text = track.collectionName
        }
    }

    private fun startTimer () {
        handler.post(updateTimerTask())
    }

    private fun updateTimerTask () : Runnable {
        return object : Runnable {
            override fun run() {
                if (newPlayerState == PlayerState.PLAYING) {
                    binding.playTime.text = viewModel.getTimer()
                    handler.postDelayed(this, TIMER_DELAY_MILLIS)
                } else {
                    handler.removeCallbacks(this)
                }
            }
        }
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 500L
    }

}