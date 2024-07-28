package com.example.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.search.network.NetworkClient
import com.example.playlistmaker.data.search.network.RetrofitNetworkClient
import com.example.playlistmaker.data.search.network.SearchiTunesApi
import com.example.playlistmaker.data.sharing.impl.ExternalNavigatorImpl
import com.example.playlistmaker.data.storage.SheredPrefStorageClient
import com.example.playlistmaker.data.storage.StorageClient
import com.example.playlistmaker.data.storage.TRACK_LIST_KEY
import com.example.playlistmaker.domain.sharing.ExternalNavigator
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<SearchiTunesApi> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(SearchiTunesApi::class.java)
    }

    single {
        androidContext().getSharedPreferences(TRACK_LIST_KEY, Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<StorageClient> {
        SheredPrefStorageClient(get(), get())
    }

    single<ExternalNavigator> {
        ExternalNavigatorImpl(androidContext())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }
}