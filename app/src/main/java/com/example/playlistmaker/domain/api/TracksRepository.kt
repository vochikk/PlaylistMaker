package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksRepository {
    fun searchTracks (expression : String) : List<Track>?

    fun saveTracksList (trackList: List<Track>)

    fun getTracksList () : List<Track>

    fun saveTrack (track: Track)

    fun clearTracksList ()
}