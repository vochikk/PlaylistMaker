package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.converter.TrackDtoConverter
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.data.storage.StorageClient
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val MAX_COUNT = 10

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storageClient: StorageClient,
    private val converter: TrackDtoConverter,
    private val appDatabase: AppDatabase): TracksRepository {

    override fun searchTracks(expression: String): Flow<List<Track>?> = flow {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        when (response.resultCode) {
            200 -> {
                val list = converter.mapToTrack((response as TracksSearchResponse).results)
                val idList = appDatabase.trackDao().getIdListTrack()
                list.map {track ->
                    idList.forEach {
                        if (it == track.trackId) track.isFavorite = true
                    }
                }
                emit(list)
            }
            400 -> {emit(emptyList())}
            else -> {emit(null)}
        }
    }

    override fun getTracksList(): List<Track> {
        return converter.mapToTrack(storageClient.get())
    }

    override fun saveTracksList (trackList: List<Track>) {
        val list = converter.mapToTrackDto(trackList)
        storageClient.save(list)
    }

    override fun saveTrack(track: Track) {
        val list = getTracksList().toMutableList()

        when {
            list.isEmpty() -> list.add(0, track)
            list.size == MAX_COUNT -> {
                var check = false
                list.forEach{
                    if (track.trackId == it.trackId) {
                        check = true
                    }
                }
                if (check) {
                    list.remove(track)
                } else {
                    list.removeAt(9)
                }
                list.add(0, track)
            }
            list.size < MAX_COUNT -> {
                list.remove(track)
                list.add(0, track)
            }
        }

        saveTracksList(list.toList())
    }

    override fun clearTracksList() {
        saveTracksList(emptyList())
    }

    override fun updateFavoriteTag(track: Track): Track {
        val idList = appDatabase.trackDao().getIdListTrack()
        idList.forEach {
            if (track.trackId == it) return Track(track.trackId,
                track.trackName, track.artistName, track.collectionName, track.releaseDate,
                track.primaryGenreName, track.country, track.trackTimeMillis, track.artworkUrl100,
                track.previewUrl, true)
        }
        return Track(track.trackId,
            track.trackName, track.artistName, track.collectionName, track.releaseDate,
            track.primaryGenreName, track.country, track.trackTimeMillis, track.artworkUrl100,
            track.previewUrl, false)
    }
}