package com.example.playlistmaker.data.search.impl

import com.example.playlistmaker.data.search.dto.TrackDto
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import com.example.playlistmaker.data.search.dto.TracksSearchResponse
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.data.storage.StorageClient
import com.example.playlistmaker.domain.player.models.Track

const val MAX_COUNT = 10

class TracksRepositoryImpl(
    private val networkClient: NetworkClient,
    private val storageClient: StorageClient): TracksRepository {

    override fun searchTracks(expression: String): List<Track>? {
        val response = networkClient.doRequest(TracksSearchRequest(expression))
        return when {
            (response.resultCode == 200) -> {
                mapListTrackDto((response as TracksSearchResponse).results)
            }

            response.resultCode == 0 -> {
                return null
            }
            else -> {
                emptyList()
            }
        }
    }

    override fun getTracksList(): List<Track> {
        val list = storageClient.get()
        return mapListTrackDto(list)
    }

    override fun saveTracksList (trackList: List<Track>) {
        val list = mapListTrack(trackList)
        storageClient.save(list)
    }

    override fun saveTrack(track: Track) {
        var list = getTracksList().toMutableList()

        when {
            list.isEmpty() -> list.add(0, track)
            list.size == MAX_COUNT -> {
                if (list.contains(track)){
                    list.remove(track)
                    list.add(0, track)
                } else {
                    list.removeAt(9)
                    list.add(0, track)
                }
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





    private fun mapListTrackDto (list: List<TrackDto>) : List<Track> {
        return list.map {
            Track(
                it.trackId,
                it.trackName,
                it.artistName,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.previewUrl
            )
        }
    }

    private fun mapListTrack (list: List<Track>) : List<TrackDto> {
        return list.map {
            TrackDto(
                it.trackId,
                it.trackName,
                it.artistName,
                it.collectionName,
                it.releaseDate,
                it.primaryGenreName,
                it.country,
                it.trackTimeMillis,
                it.artworkUrl100,
                it.previewUrl
            )
        }
    }
}