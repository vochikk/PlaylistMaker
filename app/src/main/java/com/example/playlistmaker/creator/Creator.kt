package com.example.playlistmaker.creator

import android.content.Context
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.settings.SettingsRepository
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.storage.SheredPrefStorageClient
import com.example.playlistmaker.domain.player.PlayerInteractor
import com.example.playlistmaker.data.player.PlayerRepository
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.data.search.TracksRepository
import com.example.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.example.playlistmaker.domain.search.impl.TracksInteractorImpl
import com.example.playlistmaker.domain.settings.SettingsInteractor
import com.example.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {
    private fun getTracksRepository (context: Context) : TracksRepository {
        return TracksRepositoryImpl (RetrofitNetworkClient(), SheredPrefStorageClient(context))
    }

    fun providesTracksInteractor (context: Context) : TracksInteractor {
        return TracksInteractorImpl(getTracksRepository(context))
    }


    private fun getPlayerRepository () : PlayerRepository {
        return PlayerRepositoryImpl()
    }

    fun providesPlayerInteractor () : PlayerInteractor {
        return PlayerInteractorImpl(getPlayerRepository())
    }


    private fun getSettingsRepository (context: Context): SettingsRepository {
        return SettingsRepositoryImpl(context)
    }

    fun providesSettingsInteractor (context: Context) : SettingsInteractor {
        return SettingsInteractorImpl(getSettingsRepository(context))
    }

    private fun getExternalNavigator (context: Context) : ExternalNavigator {
        return ExternalNavigatorImpl(context)
    }

    fun providesSharingInteractor (context: Context) : SharingInteractor {
        return SharingInteractorImpl(getExternalNavigator(context))
    }
}