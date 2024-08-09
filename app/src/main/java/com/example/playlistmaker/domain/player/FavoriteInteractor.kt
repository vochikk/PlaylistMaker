package com.example.playlistmaker.domain.player

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteInteractor {

    fun getFavoriteTacks () : Flow<List<Track>>

    fun insertTrack(track: Track)

    fun deleteTrack(track: Track)

    fun insertTrackInPlayList (track: Track)
}