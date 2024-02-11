package com.example.playlistmaker.domain.search.impl

import java.util.concurrent.Executors
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.search.TracksInteractor

class TracksInteractorImpl (private val repository: TracksRepository) : TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.searchTracks(expression))
        }
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
}