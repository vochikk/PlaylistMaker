package com.example.playlistmaker.ui.player.view_model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.player.OnStateChangeListener
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.player.state.PlayerState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val favoriteInteractor: FavoriteInteractor
    ): ViewModel() {

    private val _playerStateLiveDate = MutableLiveData<PlayerState>()
    val playerStateLiveData: LiveData<PlayerState> = _playerStateLiveDate

    private val _likeStateLiveData = MutableLiveData<Boolean>()
    val likeStateLiveData: LiveData<Boolean> = _likeStateLiveData

    private var timerJob: Job? = null
    private var newState: PlayerState = PlayerState.PREPARED()

    private fun onChange() {
        playerInteractor.setListener(object : OnStateChangeListener {
            override fun onChange(state: PlayerState) {
                newState = state
            }
        })
    }

    fun prepare(track: Track) {
        playerInteractor.prepare(track)
        onChange()
        _playerStateLiveDate.postValue(newState)
    }

    fun play() {
        playerInteractor.play()
        startTimer()
    }

    fun pause() {
        timerJob?.cancel()
        playerInteractor.pause()
        _playerStateLiveDate.postValue(newState)
    }

    fun realese() {
        timerJob?.cancel()
        timerJob = null
        _playerStateLiveDate.postValue(PlayerState.ENDING())
        playerInteractor.realese()
    }

    fun getTimer() : String {
        return playerInteractor.getTimer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerInteractor.isPlaying()) {
                delay(TIMER_DELAY_MILLIS)
                _playerStateLiveDate.postValue(PlayerState.PLAYING(getTimer()))
            }
            _playerStateLiveDate.postValue(PlayerState.PREPARED())
            timerJob?.cancel()
        }
    }

    fun updateFavorite(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            if (track.isFavorite) {
                favoriteInteractor.delteTrack(track)
                track.isFavorite = false
            } else {
                favoriteInteractor.insertTrack(track)
                track.isFavorite = true
            }
            _likeStateLiveData.postValue(track.isFavorite)
        }
    }

    companion object {
        private const val TIMER_DELAY_MILLIS = 300L
    }
}