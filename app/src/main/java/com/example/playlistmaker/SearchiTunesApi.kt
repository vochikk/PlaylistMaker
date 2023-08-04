package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.Query
import retrofit2.http.GET

interface SearchiTunesApi {
    @GET("/search?entity=song ")
    fun search (@Query("term") text: String) : Call<TracksResponse>
}