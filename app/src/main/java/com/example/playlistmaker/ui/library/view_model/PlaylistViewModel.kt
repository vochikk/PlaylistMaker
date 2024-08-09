package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.library.PlayListInteractor
import com.example.playlistmaker.domain.library.model.PlayList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val interactor: PlayListInteractor
): ViewModel() {

    private val _stateLiveData = MutableLiveData <List<PlayList>>()
    val stateLiveData: LiveData<List<PlayList>> = _stateLiveData

    init {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllPlaylists().collect{ list ->
                _stateLiveData.postValue(list)
            }
        }
    }

    fun get () {
        viewModelScope.launch(Dispatchers.IO) {
            interactor.getAllPlaylists().collect{ list ->
                _stateLiveData.postValue(list)
            }
        }
    }
}