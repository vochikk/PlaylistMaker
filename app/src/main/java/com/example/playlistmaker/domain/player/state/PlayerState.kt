package com.example.playlistmaker.domain.player.state

sealed class PlayerState (val isButtonPlay: Boolean, val progress: String) {
    class DEFAULT : PlayerState(true, "00:00")
    class PREPARED : PlayerState(true, "00:00")
    class PLAYING(progress: String) : PlayerState(false, progress)
    class PAUSING(progress: String) : PlayerState(true, progress)
    class ENDING : PlayerState(true, "00:00")
}