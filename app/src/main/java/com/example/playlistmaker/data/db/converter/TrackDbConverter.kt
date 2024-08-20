package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TrackListEntity
import com.example.playlistmaker.data.search.dto.TrackDto

class TrackDbConverter {

    fun map(track: TrackDto): TrackEntity {
        return TrackEntity(
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
            track.timestamp,
            track.timestampToPlaylist
        )
    }

    fun map(track: TrackEntity): TrackDto {
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
            false,
            track.timestamp,
            track.timestampToPlaylist
        )
    }

    fun mapToTrackList(track: TrackDto): TrackListEntity {
        return TrackListEntity(
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
            track.timestamp,
            track.timestampToPlaylist
        )
    }
}