package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.models.Track

interface PlayerInteractor : Player {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun realese()
    fun getTimer() : String
}