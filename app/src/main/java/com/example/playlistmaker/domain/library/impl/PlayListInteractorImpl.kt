package com.example.playlistmaker.domain.library.impl

import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.library.PlayListInteractor
import com.example.playlistmaker.domain.library.PlayListRepository
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track
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

    override fun getTrackList(id: Int): List<Track> {
        return repository.getTrackList(id)
    }

    override fun getPlaylist(id: Int): PlayList {
        return repository.getPlaylist(id)
    }

    override fun deleteTrack(trackId: Int, idPlayList: Int) {
        repository.deleteTrack(trackId, idPlayList)
    }

    override fun deletePlaylist(id: Int) {
        repository.deletePlaylist(id)
    }

    override fun updatePlaylist(playList: PlayList) {
        repository.updatePlaylist(playList)
    }
}