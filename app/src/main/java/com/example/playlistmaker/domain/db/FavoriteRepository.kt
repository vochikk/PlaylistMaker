package com.example.playlistmaker.domain.db

import com.example.playlistmaker.data.search.dto.TrackDto
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getFavoriteTacks(): Flow<List<TrackDto>>

    fun insertTrack(trackDto: TrackDto)

    fun delteTrack(trackDto: TrackDto)
}