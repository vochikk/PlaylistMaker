package com.example.playlistmaker.domain.player

import com.example.playlistmaker.data.search.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getFavoriteTacks(): Flow<List<TrackDto>>

    fun insertTrack(trackDto: TrackDto)

    fun deleteTrack(trackDto: TrackDto)

    fun insertTrackInPlayList (trackDto: TrackDto)
}