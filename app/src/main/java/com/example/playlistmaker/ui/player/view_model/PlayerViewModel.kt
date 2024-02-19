package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.OnStateChangeListener
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.player.state.PlayerState

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
    ): ViewModel() {

    private val playerStateLiveDate = MutableLiveData<PlayerState>()

    fun getPlayerStateLiveData(): LiveData<PlayerState> = playerStateLiveDate

    private fun onChange() {
        playerInteractor.setListener(object : OnStateChangeListener {
            override fun onChange(state: PlayerState) {
                playerStateLiveDate.postValue(state)
            }
        })
    }

    fun prepare(track: Track) {
        playerInteractor.prepare(track)
        onChange()
    }

    fun play() {
        playerInteractor.play()
    }

    fun pause() {
        playerInteractor.pause()
    }

    fun realese() {
        playerInteractor.realese()
    }

    fun getTimer() : String {
        return playerInteractor.getTimer()
    }
}