package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface PlayerInteractor : Player {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun realese()
    fun getTimer() : String
}