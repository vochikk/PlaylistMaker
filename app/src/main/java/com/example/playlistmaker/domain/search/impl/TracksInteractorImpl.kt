package com.example.playlistmaker.domain.search.impl

import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import kotlinx.coroutines.flow.Flow

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor {

    override fun searchTracks(expression: String): Flow<List<Track>?> {
        return repository.searchTracks(expression)
    }

    override fun getTracksList(): List<Track> {
        return repository.getTracksList()
    }

    override fun saveTracksList(trackList: List<Track>) {
        repository.saveTracksList(trackList)
    }

    override fun saveTrack(track: Track) {
        repository.saveTrack(track)
    }

    override fun clearTracksList() {
        repository.clearTracksList()
    }

    override fun updateFavoriteTag(track: Track): Track {
        return repository.updateFavoriteTag(track)
    }
}