package com.example.playlistmaker.data.storage

import com.example.playlistmaker.data.dto.TrackDto

interface StorageClient {
    fun save (trackList: List<TrackDto>) : Boolean

    fun get () : List<TrackDto>
}