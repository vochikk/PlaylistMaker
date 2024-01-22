package com.example.playlistmaker.domain.api

interface Player {
    fun setListener (onStateChangeListrner: OnStateChangeListener)
}