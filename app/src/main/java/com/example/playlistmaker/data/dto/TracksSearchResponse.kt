package com.example.playlistmaker.data.dto

class TracksSearchResponse (
    val resultCount: Short,
    val results: List<TrackDto>) : Response ()