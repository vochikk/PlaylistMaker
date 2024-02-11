package com.example.playlistmaker.data.search.dto

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TrackDto

class TracksSearchResponse (
    val resultCount: Short,
    val results: List<TrackDto>) : Response()