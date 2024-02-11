package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.player.state.PlayerState

interface OnStateChangeListener {
    fun onChange(state: PlayerState)
}