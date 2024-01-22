package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    interface TracksConsumer {
        fun consume(searchTracks: List<Track>?)
    }

    fun getTracksList(): List<Track>

    fun saveTracksList (trackList: List<Track>)

    fun saveTrack(track: Track)

    fun clearTracksList()
}
