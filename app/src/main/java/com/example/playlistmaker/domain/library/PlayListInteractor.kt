package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    fun insertPlaylist(playList: PlayList)

    fun getAllPlaylists(): Flow<List<PlayList>>

    fun updateTracksList(playListEntity: PlayList)
}