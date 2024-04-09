package com.example.playlistmaker.data.search.network

import com.example.playlistmaker.data.search.dto.Response
import com.example.playlistmaker.data.search.dto.TracksSearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.Exception

class RetrofitNetworkClient (
    private val itunesSearchService: SearchiTunesApi
    ): NetworkClient {

    override suspend fun doRequest(dto: Any) : Response {
        if (dto !is TracksSearchRequest) {
            return Response().apply { resultCode = 400 } //произошла ошибка
        }

        return withContext(Dispatchers.IO) {
            try {
                val response = itunesSearchService.search(dto.expression)
                response.apply { resultCode = 200 }//список треков
            } catch (e: Exception) {
                Response().apply { resultCode = 500 }//ничего не найдено
            }
        }
    }
}