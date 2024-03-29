package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RetrofitNetworkClient (
    private val itunesSearchService: SearchiTunesApi
    ): NetworkClient {

    override fun doRequest(dto: Any) : Response {
        try {
            if (dto is TracksSearchRequest) {
                val resp = itunesSearchService.search(dto.expression).execute()

                val body: Response = resp.body() ?: Response()

                return body.apply { resultCode = resp.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        } catch (e: Exception) {
            return Response().apply { resultCode = 0 }
        }
    }
}