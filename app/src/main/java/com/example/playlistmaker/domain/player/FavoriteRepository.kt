package com.example.playlistmaker.domain.player

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {

    fun getFavoriteTacks(): Flow<List<TrackDto>>

    fun insertTrack(trackDto: TrackDto)

    fun deleteTrack(trackDto: TrackDto)

    fun insertTrackInPlayList (playList: PlayList, trackDto: TrackDto)

    fun getTrackList(playList: PlayList) : Flow<List<Int>>
}