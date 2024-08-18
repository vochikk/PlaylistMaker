package com.example.playlistmaker.data.db.impl

import android.util.Log
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.PlayListDbConverter
import com.example.playlistmaker.data.db.entity.TracksToPlaylistEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.library.PlayListRepository
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.math.abs

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

    override fun getTrackList(id: Int): List<Track> {
        val list = appDatabase.tracksToPlaylistDao().getTrackList(id)
        val trackList = appDatabase.trackListDao().getTrackList()
        val tracksInPlaylist = mutableListOf<Track>()
        trackList.forEach {
            if (list.contains(it.trackId)){
                tracksInPlaylist.add(converter.mapToTrack(it))
            }
        }
        return tracksInPlaylist
    }

    override fun getPlaylist(id: Int): PlayList {
        return converter.map(appDatabase.playlistDao().getPlaylist(id))
    }

    override fun deleteTrack(trackId: Int, idPlayList: Int) {
        appDatabase.tracksToPlaylistDao().deleteTrack(TracksToPlaylistEntity(idPlayList, trackId))
        val list = appDatabase.tracksToPlaylistDao().getPlaylistByIdTrack(trackId)
        if (list.isEmpty()) {
            appDatabase.trackListDao().delete(trackId)
        }
        val size = appDatabase.tracksToPlaylistDao().getTrackList(idPlayList).size
        appDatabase.playlistDao().updatePlaylist(idPlayList, size)
    }

    override fun deletePlaylist(id: Int) {
        val list = appDatabase.tracksToPlaylistDao().getTrackList(id)
        appDatabase.tracksToPlaylistDao().delete(id)
        appDatabase.playlistDao().delete(id)


        list.forEach {
            val trackList = appDatabase.tracksToPlaylistDao().getPlaylistByIdTrack(it)
            if (trackList.isEmpty()) {
                appDatabase.trackListDao().delete(it)
            }
        }
    }

    override fun updatePlaylist(playList: PlayList) {
        appDatabase.playlistDao().updatePlaylist(converter.map(playList))
    }
}