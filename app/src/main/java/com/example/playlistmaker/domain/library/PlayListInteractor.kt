package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListInteractor {

    fun insertPlaylist(playList: PlayList)

    fun getAllPlaylists(): Flow<List<PlayList>>

    fun updateTracksList(playListEntity: PlayList)

    fun getTrackList (id: Int) : List<Track>

    fun getPlaylist (id: Int): PlayList

    fun deleteTrack (trackId: Int, idPlayList: Int)

    fun deletePlaylist (id: Int)

    fun updatePlaylist (playList: PlayList)
}