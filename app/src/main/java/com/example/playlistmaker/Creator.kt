package com.example.playlistmaker

import android.content.Context
import com.example.playlistmaker.data.PlayerRepositoryImpl
import com.example.playlistmaker.data.TracksRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.storage.SheredPrefStorageClient
import com.example.playlistmaker.domain.api.OnStateChangeListener
import com.example.playlistmaker.domain.api.PlayerInteractor
import com.example.playlistmaker.domain.api.PlayerRepository
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.impl.TracksInteractorImpl

object Creator {
    private fun getTracksRepository (context: Context) : TracksRepository {
        return TracksRepositoryImpl (RetrofitNetworkClient(), SheredPrefStorageClient(context))
    }

    private fun getPlayerRepository () : PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providesTracksInteractor (context: Context) : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }

    fun providesPlayerInteractor () : PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }
}