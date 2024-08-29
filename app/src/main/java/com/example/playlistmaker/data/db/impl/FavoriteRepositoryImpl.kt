package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.PlayListDbConverter
import com.example.playlistmaker.domain.player.FavoriteRepository
import com.example.playlistmaker.data.db.converter.TrackDbConverter
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TracksToPlaylistEntity
import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
) : FavoriteRepository {

    override fun getFavoriteTacks(): Flow<List<TrackDto>> = flow {

        val tracks = appDatabase.trackDao().getListTrack().sortedBy { it.timestamp }
        emit(convertFromTrackEntity(tracks).reversed())
    }

    override fun insertTrack(trackDto: TrackDto) {
        var track = trackDbConverter.map(trackDto)
        track.timestamp = System.currentTimeMillis()
        appDatabase.trackDao().insertTrack(track)
    }

    override fun deleteTrack(trackDto: TrackDto) {
        appDatabase.trackDao().deleteTrack(trackDbConverter.map(trackDto))
    }

    override fun insertTrackInPlayList(playList: PlayList, trackDto: TrackDto) {
        appDatabase.trackListDao().insertTrack(trackDbConverter.mapToTrackList(trackDto))
        appDatabase.tracksToPlaylistDao()
            .insert(TracksToPlaylistEntity(playList.idPlaylist, trackDto.trackId))
    }

    override fun getTrackList(playList: PlayList): Flow<List<Int>> = flow{
        emit(appDatabase.tracksToPlaylistDao().getTrackList(playList.idPlaylist))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<TrackDto> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }
}