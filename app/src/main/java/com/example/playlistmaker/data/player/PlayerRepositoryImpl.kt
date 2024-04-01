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
    private var timerJob: Job? = null

    override fun setListener(onStateChangeListrner: OnStateChangeListener) {
        listener = onStateChangeListrner
    }

    override fun prepare(track: Track){
        mediaPlayer = MediaPlayer()
        mediaPlayer?.setDataSource(track.previewUrl)
        mediaPlayer?.prepare()

        mediaPlayer?.setOnPreparedListener {
            listener?.onChange(PlayerState.PREPARED())
        }

        mediaPlayer?.setOnCompletionListener {
            listener?.onChange(PlayerState.ENDING())
        }
    }

    override fun play() {
        mediaPlayer?.start()
        listener?.onChange(PlayerState.PLAYING(getTimer()))
        startTimer()
    }

    override fun pause() {
        mediaPlayer?.pause()
        timerJob?.cancel()
        listener?.onChange(PlayerState.PAUSING(getTimer()))
    }

    override fun realese() {
        mediaPlayer?.release()
        timerJob?.cancel()
        listener?.onChange(PlayerState.ENDING())
    }

    private fun getTimer() : String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(mediaPlayer?.currentPosition)
    }

    private fun startTimer() {
        timerJob = GlobalScope.launch {
            while (mediaPlayer!!.isPlaying) {
                delay(TIMER_DELAY_MILLIS)
                listener?.onChange(PlayerState.PLAYING(getTimer()))
            }
        }
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }

}