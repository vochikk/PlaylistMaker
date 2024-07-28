package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface TracksRepository {
    fun searchTracks (expression : String) : Flow<List<Track>?>

    fun saveTracksList (trackList: List<Track>)

    fun getTracksList () : List<Track>

    fun saveTrack (track: Track)

    fun clearTracksList ()

    fun updateFavoriteTag(track: Track) : Track
}