package com.example.playlistmaker.ui.search.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.di.viewModelModule
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.domain.search.TracksInteractor
import com.example.playlistmaker.ui.search.state.SearchState
import com.example.playlistmaker.utils.debounce
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val tracksInteractor: TracksInteractor
): ViewModel() {

    private var _searchStateLiveData = MutableLiveData<SearchState>()
    val searchStateLiveData: LiveData<SearchState> = _searchStateLiveData

    private var latestSearchText: String? = null

    private val trackSearchDebounce = debounce<String> (SEARCH_DEBOUNCE_DELAY_MILLIS,
                                                        viewModelScope,
                                            true) { changedText ->
        searchRequest(changedText)
    }

    fun searchDebounce (changedText: String) {
        if (latestSearchText != changedText) {
            latestSearchText = changedText
            trackSearchDebounce(changedText)
        }
    }

    fun searchRequest (expression: String) {
        if (expression.isNotEmpty()) {

            _searchStateLiveData.postValue(SearchState.isLoading)

            viewModelScope.launch(Dispatchers.IO) {
                tracksInteractor
                    .searchTracks(expression)
                    .collect{tracks ->
                        processResult(tracks)}
            }
        } else _searchStateLiveData.postValue(SearchState.HistoryView)
    }

    fun getTracksList(): List<Track> {
        val trackList = tracksInteractor.getTracksList()
        trackList.forEach{
            updateFavoriteTag(it)
        }
        return trackList
    }

    private fun saveTrack(track: Track) {
        updateFavoriteTag(track)
    }

    fun clearTracksList() {
        tracksInteractor.clearTracksList()
    }

    fun updateFavoriteTag(track: Track) {
        viewModelScope.launch(Dispatchers.IO) {
            tracksInteractor.saveTrack(tracksInteractor.updateFavoriteTag(track))
        }

    }

    private fun processResult(searchTracks: List<Track>?) {
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }
}