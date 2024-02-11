package com.example.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.OnStateChangeListener
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.player.state.PlayerState

class PlayerViewModel: ViewModel() {

    private val playerInteractor = Creator.providesPlayerInteractor()

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

    companion object {
        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                PlayerViewModel()
            }
        }
    }

}