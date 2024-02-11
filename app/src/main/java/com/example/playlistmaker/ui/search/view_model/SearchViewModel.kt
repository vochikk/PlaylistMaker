package com.example.playlistmaker.ui.search.view_model

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.ui.search.state.SearchState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var searchStateLiveData = MutableLiveData<SearchState>()

    fun getSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce (changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        searchStateLiveData.postValue(SearchState.isLoading)

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    fun searchRequest (expression: String) {
        tracksInteractor.searchTracks(expression,
            consumer = object : TracksInteractor.TracksConsumer{
                override fun consume(searchTracks: List<Track>?) {
                    if (searchTracks == null) {
                        searchStateLiveData.postValue(SearchState.Error)
                    } else {
                        if (searchTracks!!.isNotEmpty()) {
                            searchStateLiveData.postValue(SearchState.Content(searchTracks))
                        } else {
                            searchStateLiveData.postValue(SearchState.NonFound)
                        }
                    }
                }
            })
    }

    fun getTracksList(): List<Track> {
        return tracksInteractor.getTracksList()
    }

    fun saveTrack(track: Track) {
        tracksInteractor.saveTrack(track)
    }

    fun clearTracksList() {
        tracksInteractor.clearTracksList()
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()

        fun getSearchViewModel(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val tracksInteractor = Creator.providesTracksInteractor(context)

                SearchViewModel(tracksInteractor)
            }
        }
    }
}