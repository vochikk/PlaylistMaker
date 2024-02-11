package com.example.playlistmaker.data.search

import com.example.playlistmaker.domain.player.models.Track

interface TracksRepository {
    fun searchTracks (expression : String) : List<Track>?

    fun saveTracksList (trackList: List<Track>)

    fun getTracksList () : List<Track>

    fun saveTrack (track: Track)

    fun clearTracksList ()
}