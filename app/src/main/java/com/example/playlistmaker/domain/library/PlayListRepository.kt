package com.example.playlistmaker.domain.library

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    fun insertPlaylist (playList: PlayList)

    fun getAllPlaylists (): Flow<List<PlayList>>

    fun updateTracksList (playList: PlayList)
}