package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.data.db.FavoriteRepository
import com.example.playlistmaker.data.db.converter.TrackDtoConverter
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteInteractorImpl (
    private val repository: FavoriteRepository,
    private val converter: TrackDtoConverter
        ) : FavoriteInteractor {


    override fun getFavoriteTacks(): List<Track>{
        return converter.mapToTrack(repository.getFavoriteTacks())
    }

    override fun insertTrack(track: Track) {
        repository.insertTrack(converter.mapTrack(track))
    }

    override fun delteTrack(track: Track) {
        repository.delteTrack(converter.mapTrack(track))
    }
}