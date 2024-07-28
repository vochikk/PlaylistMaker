package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    fun getFavoriteTacks () : List<Track>

    fun insertTrack(track: Track)

    fun delteTrack(track: Track)
}