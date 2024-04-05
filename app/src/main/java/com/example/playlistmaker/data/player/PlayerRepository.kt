package com.example.playlistmaker.data.player

import com.example.playlistmaker.domain.player.Player
import com.example.playlistmaker.domain.player.models.Track

interface PlayerRepository : Player {
    fun prepare(track: Track)
    fun play()
    fun pause()
    fun realese()
    fun isPlaying() : Boolean
    fun getTimer() : String
}