package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.domain.db.PlayListInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistViewModel(
    private val interactor: PlayListInteractor
): ViewModel() {

    private val _stateLiveData = MutableLiveData <List<PlayListEntity>>()
    val stateLiveData: LiveData<List<PlayListEntity>> = _stateLiveData

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