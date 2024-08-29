package com.example.playlistmaker.domain.library

import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow

interface PlayListRepository {

    fun insertPlaylist (playList: PlayList)

    fun getAllPlaylists (): Flow<List<PlayList>>

    fun updateTracksList (playList: PlayList)

    fun getTrackList (id: Int) : List<Track>

    fun getPlaylist (id: Int): PlayList

    fun deleteTrack (trackId: Int, idPlayList: Int)

    fun deletePlaylist (id: Int)

    fun updatePlaylist (playList: PlayList)
}