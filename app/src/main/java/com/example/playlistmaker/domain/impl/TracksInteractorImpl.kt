package com.example.playlistmaker.domain.impl

import java.util.concurrent.Executors
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.Track

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