package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlayListRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase
): PlayListRepository {

    override fun insertPlaylist(playList: PlayListEntity) {
        appDatabase.playlistDao().insertPlaylist(playList)
    }

    override fun getAllPlaylists(): Flow<List<PlayListEntity>> = flow{
        emit(appDatabase.playlistDao().getAllPlaylists())
    }

    override fun updateTracksList(playList: PlayListEntity) {
        appDatabase.playlistDao().updateTracksList(playList)
    }
}