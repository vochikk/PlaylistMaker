package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.PlayerState

interface OnStateChangeListener {
    fun onChange(state: PlayerState)
}