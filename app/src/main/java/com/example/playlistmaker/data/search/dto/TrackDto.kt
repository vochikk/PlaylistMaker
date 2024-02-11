package com.example.playlistmaker.data.search.dto

import java.text.SimpleDateFormat
import java.util.Locale

data class TrackDto (
    val trackId: Long,
    val trackName: String,
    val artistName: String,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val trackTimeMillis: Long,
    val artworkUrl100: String,
    val previewUrl: String
)