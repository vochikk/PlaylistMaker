package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.OnStateChangeListener
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.domain.player.models.Track

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