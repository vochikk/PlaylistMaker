package com.example.playlistmaker.domain.db.impl

import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.data.db.converter.TrackDtoConverter
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavoriteInteractorImpl(
    private val repository: FavoriteRepository,
    private val converter: TrackDtoConverter
) : FavoriteInteractor {

    override fun getFavoriteTacks(): Flow<List<Track>> {
        return repository.getFavoriteTacks().map { tracks ->
            converter.mapToTrack(tracks)
        }
    }

    override fun insertTrack(track: Track) {
        repository.insertTrack(converter.mapTrack(track))
    }

    override fun delteTrack(track: Track) {
        repository.delteTrack(converter.mapTrack(track))
    }
}