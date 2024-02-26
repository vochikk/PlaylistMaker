package com.example.playlistmaker.data.player

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.OnStateChangeListener
import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.state.PlayerState
import com.example.playlistmaker.domain.player.models.Track
import java.util.Locale

class PlayerRepositoryImpl () : PlayerRepository, Player {
    private var mediaPlayer: MediaPlayer? = null
    private var listener : OnStateChangeListener? = null

    override fun setListener(onStateChangeListrner: OnStateChangeListener) {
        listener = onStateChangeListrner
    }

    override fun prepare(track: Track){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(track.previewUrl)
        mediaPlayer?.prepare()

        mediaPlayer?.setOnPreparedListener {
            listener?.onChange(PlayerState.PREPARED)
        }

        mediaPlayer?.setOnCompletionListener {
            listener?.onChange(PlayerState.ENDING)
        }
    }

    override fun play() {
        mediaPlayer?.start()
        listener?.onChange(PlayerState.PLAYING)
    }

    override fun pause() {
        mediaPlayer?.pause()
        listener?.onChange(PlayerState.PAUSING)
    }

    override fun realese() {
        mediaPlayer?.release()
        listener?.onChange(PlayerState.ENDING)
    }


    override fun getTimer() : String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
    }

}