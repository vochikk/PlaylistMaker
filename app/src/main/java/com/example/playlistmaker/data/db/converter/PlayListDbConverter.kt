package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.data.db.entity.TrackListEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track

class PlayListDbConverter {

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playList.idPlaylist,
            playList.namePlaylist,
            playList.about,
            playList.imageUri,
            playList.sizePlaylist
        )
    }

    fun map(playListEntity: PlayListEntity): PlayList {
        return PlayList(
            playListEntity.idPlaylist,
            playListEntity.namePlaylist,
            playListEntity.about,
            playListEntity.imageUri,
            playListEntity.sizePlaylist
        )
    }

    fun mapToTrack(trackListEntity: TrackListEntity): Track {
        return Track(
            trackListEntity.trackId,
            trackListEntity.trackName,
            trackListEntity.artistName,
            trackListEntity.collectionName,
            trackListEntity.releaseDate,
            trackListEntity.primaryGenreName,
            trackListEntity.country,
            trackListEntity.trackTimeMillis,
            trackListEntity.artworkUrl100,
            trackListEntity.previewUrl,
            false
        )
    }
}