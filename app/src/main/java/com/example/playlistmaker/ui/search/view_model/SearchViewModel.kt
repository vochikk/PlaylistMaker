package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.ui.search.state.SearchState

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
): ViewModel() {

    private val handler = Handler(Looper.getMainLooper())
    private var _searchStateLiveData = MutableLiveData<SearchState>()
    val searchStateLiveData: LiveData<SearchState> = _searchStateLiveData

    private var latestSearchText: String? = null

    override fun onCleared() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
    }

    fun searchDebounce (changedText: String) {
        if (latestSearchText == changedText) {
            return
        }

        this.latestSearchText = changedText
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)

        val searchRunnable = Runnable { searchRequest(changedText) }

        val postTime = SystemClock.uptimeMillis() + SEARCH_DEBOUNCE_DELAY_MILLIS
        handler.postAtTime(
            searchRunnable,
            SEARCH_REQUEST_TOKEN,
            postTime
        )
    }

    fun searchRequest (expression: String) {
        if (expression.isNotEmpty()) {

            _searchStateLiveData.postValue(SearchState.isLoading)

            tracksInteractor.searchTracks(expression,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(searchTracks: List<Track>?) {
                        if (searchTracks == null) {
                            _searchStateLiveData.postValue(SearchState.Error)
                        } else {
                            if (searchTracks!!.isNotEmpty()) {
                                _searchStateLiveData.postValue(SearchState.Content(searchTracks))
                            } else {
                                _searchStateLiveData.postValue(SearchState.NonFound)
                            }
                        }
                    }
                })
        } else _searchStateLiveData.postValue(SearchState.HistoryView)
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
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
        private val SEARCH_REQUEST_TOKEN = Any()
    }
}