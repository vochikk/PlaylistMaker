package com.example.playlistmaker.data.network

import com.example.playlistmaker.data.dto.TracksSearchResponse
import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.GET

interface SearchiTunesApi {
    @GET("/search?entity=song ")
    fun search (@Query("term") text: String) : Call<TracksSearchResponse>
}