package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.OnStateChangeListener

interface Player {
    fun setListener (onStateChangeListrner: OnStateChangeListener)
}