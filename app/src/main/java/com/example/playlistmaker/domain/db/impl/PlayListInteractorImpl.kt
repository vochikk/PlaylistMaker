package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlayListInteractor
import com.example.playlistmaker.domain.db.PlayListRepository
import kotlinx.coroutines.flow.Flow

class PlayListInteractorImpl(
    val repository: PlayListRepository
): PlayListInteractor {

    override fun insertPlaylist(playList: PlayListEntity) {
        repository.insertPlaylist(playList)
    }

    override fun getAllPlaylists(): Flow<List<PlayListEntity>> {
        return repository.getAllPlaylists()
    }

    override fun updateTracksList(playListEntity: PlayListEntity) {
        repository.updateTracksList(playListEntity)
    }
}