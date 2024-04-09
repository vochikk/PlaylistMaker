package com.example.playlistmaker.domain.player.state

import android.icu.text.SimpleDateFormat
import java.util.Locale

sealed class PlayerState (val isButtonPlay: Boolean, val progress: String) {
    class PREPARED : PlayerState(true, SimpleDateFormat("mm:ss", Locale.getDefault()).format(0))
    class PLAYING(progress: String) : PlayerState(false, progress)
    class PAUSING(progress: String) : PlayerState(true, progress)
    class ENDING : PlayerState(true, SimpleDateFormat("mm:ss", Locale.getDefault()).format(0))
}