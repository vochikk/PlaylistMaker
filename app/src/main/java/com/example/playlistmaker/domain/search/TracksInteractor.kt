package com.example.playlistmaker.domain.search

import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {
    fun searchTracks(expression: String): Flow<List<Track>?>

    fun getTracksList(): List<Track>

    fun saveTracksList (trackList: List<Track>)

    fun saveTrack(track: Track)

    fun clearTracksList()

}
