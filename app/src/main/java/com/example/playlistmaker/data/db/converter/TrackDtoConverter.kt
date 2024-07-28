package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.player.models.Track

class TrackDtoConverter {

    fun mapToTrack (list: List<TrackDto>) : List<Track> {
        return list.map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.previewUrl,
                it.isFavorite
            )
        }
    }

    fun mapToTrackDto (list: List<Track>) : List<TrackDto> {
        return list.map {
            TrackDto(
                it.trackId,
                it.trackName,
                it.artistName,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.previewUrl,
                it.isFavorite
            )
        }
    }

    fun mapTrack (track: Track) : TrackDto {
        return TrackDto(
            track.trackId,
            track.trackName,
            track.artistName,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.previewUrl,
            track.isFavorite
        )
    }
}