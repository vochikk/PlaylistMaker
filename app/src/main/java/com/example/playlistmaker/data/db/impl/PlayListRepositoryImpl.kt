package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.PlayListDbConverter
import com.example.playlistmaker.domain.library.PlayListRepository
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: PlayListDbConverter
) : PlayListRepository {

    override fun insertPlaylist(playList: PlayList) {
        appDatabase.playlistDao().insertPlaylist(converter.map(playList))
    }

    override fun getAllPlaylists(): Flow<List<PlayList>> = flow {
        emit(appDatabase.playlistDao().getAllPlaylists().map {
            converter.map(it)
        })
    }

    override fun updateTracksList(playList: PlayList) {
        appDatabase.playlistDao().updateTracksList(converter.map(playList))
    }
}