package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavoriteInteractor
import com.example.playlistmaker.domain.player.models.Track
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val interactor: FavoriteInteractor
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<List<Track>>()
    val stateLiveData: LiveData<List<Track>> = _stateLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            getContent().collect { tracks ->
                _stateLiveData.postValue(tracks)
            }
        }
    }


    private fun getContent(): Flow<List<Track>> {
        return interactor.getFavoriteTacks()
    }

    fun updateList() {
        viewModelScope.launch(Dispatchers.IO) {
            getContent().collect { tracks ->
                _stateLiveData.postValue(tracks)
            }
        }
    }
}