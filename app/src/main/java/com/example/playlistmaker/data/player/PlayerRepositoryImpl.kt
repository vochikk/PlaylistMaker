package com.example.playlistmaker.data.player

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.OnStateChangeListener
import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.state.PlayerState
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

class PlayerRepositoryImpl () : PlayerRepository, Player {
    private var mediaPlayer: MediaPlayer? = null
    private var listener : OnStateChangeListener? = null
    private val dataFormat by lazy { SimpleDateFormat("mm:ss", Locale.getDefault()) }

    override fun setListener(onStateChangeListener: OnStateChangeListener) {
        listener = onStateChangeListener
    }

    override fun prepare(track: Track){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(track.previewUrl)
        mediaPlayer?.prepare()

        mediaPlayer?.setOnPreparedListener {
            listener?.onChange(PlayerState.PREPARED())
        }

        mediaPlayer?.setOnCompletionListener {
            listener?.onChange(PlayerState.PREPARED())
        }
    }

    override fun play() {
        mediaPlayer?.start()
        listener?.onChange(PlayerState.PLAYING(getTimer()))
    }

    override fun pause() {
        mediaPlayer?.pause()
        listener?.onChange(PlayerState.PAUSING(getTimer()))
    }

    override fun realese() {
        mediaPlayer?.release()
        listener?.onChange(PlayerState.ENDING())
        listener = null

    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun getTimer() : String {
        return dataFormat.format(mediaPlayer?.currentPosition)
    }

}