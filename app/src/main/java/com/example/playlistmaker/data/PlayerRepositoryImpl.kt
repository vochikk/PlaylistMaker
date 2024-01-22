package com.example.playlistmaker.data

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.OnStateChangeListener
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track
import java.util.Locale

class PlayerRepositoryImpl () : PlayerRepository, Player {
    private val mediaPlayer = MediaPlayer()
    private var listener : OnStateChangeListener? = null

    override fun setListener(onStateChangeListrner: OnStateChangeListener) {
        listener = onStateChangeListrner
    }

    override fun prepare(track: Track){
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepare()

        mediaPlayer.setOnPreparedListener {
            listener?.onChange(PlayerState.PREPARED)
        }

        mediaPlayer.setOnCompletionListener {
            listener?.onChange(PlayerState.ENDING)
        }
    }

    override fun play() {
        mediaPlayer.start()
        listener?.onChange(PlayerState.PLAYING)
    }

    override fun pause() {
        mediaPlayer.pause()
        listener?.onChange(PlayerState.PAUSING)
    }

    override fun realese() {
        mediaPlayer.release()
        listener?.onChange(PlayerState.ENDING)
    }


    override fun getTimer() : String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer.currentPosition)
    }

}