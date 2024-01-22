package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState
import com.example.playlistmaker.domain.models.Track

interface PlayerRepository :  Player {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun realese()
    fun getTimer() : String
}