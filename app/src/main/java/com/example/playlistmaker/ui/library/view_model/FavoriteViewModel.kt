package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class FavoriteViewModel(val isVisible: Boolean): ViewModel() {

    private val _stateLiveData = MutableLiveData(isVisible)
    val stateLiveData: LiveData<Boolean> = _stateLiveData
}