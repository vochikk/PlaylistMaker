package com.example.playlistmaker.di

import com.example.playlistmaker.domain.db.FavoriteRepository
import com.example.playlistmaker.data.db.converter.TrackDbConverter
import com.example.playlistmaker.data.db.converter.TrackDtoConverter
import com.example.playlistmaker.data.db.impl.FavoriteRepositoryImpl
import com.example.playlistmaker.data.db.impl.PlayListRepositoryImpl
import com.example.playlistmaker.domain.player.PlayerRepository
import com.example.playlistmaker.data.player.PlayerRepositoryImpl
import com.example.playlistmaker.domain.search.TracksRepository
import com.example.playlistmaker.data.search.impl.TracksRepositoryImpl
import com.example.playlistmaker.data.settings.SettingsRepositoryImpl
import com.example.playlistmaker.domain.db.PlayListRepository
import com.example.playlistmaker.domain.settings.SettingsRepository
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module {

    single<PlayerRepository> {
        PlayerRepositoryImpl()
    }

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(androidContext())
    }

    single<FavoriteRepository> {
        FavoriteRepositoryImpl(get(), get())
    }

    single<PlayListRepository> {
        PlayListRepositoryImpl(get())
    }

    factory { TrackDbConverter() }

    factory { TrackDtoConverter() }
}