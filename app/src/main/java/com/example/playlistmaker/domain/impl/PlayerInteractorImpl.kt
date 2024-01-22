package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.OnStateChangeListener
import com.example.playlistmaker.domain.api.Player
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.models.Track

class PlayerInteractorImpl (private val playerRepository: PlayerRepository) : PlayerInteractor {

    override fun setListener(onStateChangeListrner: OnStateChangeListener) {
        playerRepository.setListener(onStateChangeListrner)
    }

    override fun prepare(track: Track) {
        playerRepository.prepare(track)
    }

    override fun play() {
        playerRepository.play()
    }

    override fun pause() {
        playerRepository.pause()
    }

    override fun realese() {
        playerRepository.realese()
    }

    override fun getTimer(): String {
        return playerRepository.getTimer()
    }

}