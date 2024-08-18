package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.FavoriteRepository
import com.example.playlistmaker.data.db.converter.TrackDtoConverter
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.FavoriteInteractor
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

    override fun deleteTrack(track: Track) {
        repository.deleteTrack(converter.mapTrack(track))
    }

    override fun insertTrackInPlayList(playList: PlayList, track: Track) {
        repository.insertTrackInPlayList(playList, converter.mapTrack(track))
    }

    override fun getTrackList(playList: PlayList): Flow<List<Int>> {
        return repository.getTrackList(playList)
    }
}