package com.example.playlistmaker.data.db.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.FavoriteRepository
import com.example.playlistmaker.data.db.converter.TrackDbConverter
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.search.dto.TrackDto

class FavoriteRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val trackDbConverter: TrackDbConverter
        ) : FavoriteRepository {

    override fun getFavoriteTacks(): List<TrackDto> {

        val tracks = appDatabase.trackDao().getListTrack().sortedWith(compareBy{it.timestamp})
        return convertFromTrackEntity(tracks).reversed()
    }

    override fun insertTrack(trackDto: TrackDto) {
        var track = trackDbConverter.map(trackDto)
        track.timestamp = System.currentTimeMillis()
        appDatabase.trackDao().insertTrack(track)
    }

    override fun delteTrack(trackDto: TrackDto) {
        appDatabase.trackDao().deleteTrack(trackDbConverter.map(trackDto))
    }

    private fun convertFromTrackEntity (tracks: List<TrackEntity>) : List<TrackDto> {
        return tracks.map { track -> trackDbConverter.map(track) }
    }

}