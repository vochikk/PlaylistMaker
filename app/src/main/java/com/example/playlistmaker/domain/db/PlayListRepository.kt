package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.db.entity.PlayListEntity
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    fun insertPlaylist (playListEntity: PlayListEntity)

    fun getAllPlaylists (): Flow<List<PlayListEntity>>

    fun updateTracksList (playListEntity: PlayListEntity)
}