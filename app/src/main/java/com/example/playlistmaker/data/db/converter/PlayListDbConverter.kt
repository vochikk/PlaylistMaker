package com.example.playlistmaker.data.db.converter

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.library.model.PlayList

class PlayListDbConverter {

    fun map(playList: PlayList): PlayListEntity {
        return PlayListEntity(
            playList.idPlaylist,
            playList.namePlaylist,
            playList.about,
            playList.imageUri,
            playList.tracksList,
            playList.sizePlaylist
        )
    }

    fun map(playListEntity: PlayListEntity): PlayList {
        return PlayList(
            playListEntity.idPlaylist,
            playListEntity.namePlaylist,
            playListEntity.about,
            playListEntity.imageUri,
            playListEntity.tracksList,
            playListEntity.sizePlaylist
        )
    }
}