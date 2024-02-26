package com.example.playlistmaker.ui.library.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class PlaylistViewModel(val isVisible: Boolean): ViewModel() {

    private val stateLiveData = MutableLiveData<Boolean>(isVisible)
    fun observeState(): LiveData<Boolean> = stateLiveData
}