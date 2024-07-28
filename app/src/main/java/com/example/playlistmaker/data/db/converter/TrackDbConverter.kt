package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto

class TrackDbConverter {

    fun map(track: TrackDto) : TrackEntity {
        return TrackEntity(track.trackId, track.trackName, track.artistName,
            track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
            track.trackTimeMillis, track.artworkUrl100, track.previewUrl, System.currentTimeMillis())
    }

    fun map(track: TrackEntity) : TrackDto {
        return TrackDto(track.trackId, track.trackName, track.artistName,
        track.collectionName, track.releaseDate, track.primaryGenreName, track.country,
        track.trackTimeMillis, track.artworkUrl100, track.previewUrl)
    }
}