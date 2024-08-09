package com.example.playlistmaker.domain.library.impl

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.library.PlayListInteractor
import com.example.playlistmaker.domain.library.PlayListRepository
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    val repository: PlayListRepository
): PlayListInteractor {

    override fun insertPlaylist(playList: PlayList) {
        repository.insertPlaylist(playList)
    }

    override fun getAllPlaylists(): Flow<List<PlayList>> {
        return repository.getAllPlaylists()
    }

    override fun updateTracksList(playList: PlayList) {
        repository.updateTracksList(playList)
    }
}