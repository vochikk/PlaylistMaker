package com.example.playlistmaker.ui.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavoriteBinding
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.ui.library.view_holder.LibraryAdapter
import com.example.playlistmaker.ui.library.view_model.FavoriteViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment.Companion.CLICK_DEBOUNCE_DELAY_MILLIS
import com.example.playlistmaker.ui.search.fragment.SearchFragment.Companion.TRACK_KEY
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavoriteFragment: Fragment() {
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get(): FragmentFavoriteBinding = _binding!!

    private val favoriteViewModel: FavoriteViewModel by viewModel()
    private val adapter = LibraryAdapter()
    private var isClickAllowed: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.favoriteList.adapter = adapter


        favoriteViewModel.stateLiveData.observe(viewLifecycleOwner) { tracks ->
            render(tracks)
        }

        adapter.setOnClickListener(object : LibraryAdapter.OnClickListener {
            override fun onClick(track: Track) {
                if (clickDebounce()) {
                    startPlayer(track)
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        favoriteViewModel.updateList()
        adapter.notifyDataSetChanged()
        isClickAllowed = true
    }

    override fun onStart() {
        super.onStart()
        favoriteViewModel.updateList()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render(tracks: List<Track>) {
        with(binding) {
            favoriteList.visibility = View.GONE
            placeholderImage.visibility = View.GONE
            placeholderText.visibility = View.GONE
            progressBar.visibility = View.VISIBLE
        }

        if (tracks.isNotEmpty()){
                with(binding) {
                    progressBar.visibility = View.GONE
                    favoriteList.visibility = View.VISIBLE
                    placeholderImage.visibility = View.GONE
                    placeholderText.visibility = View.GONE
                }

            adapter.tracks.clear()
            adapter.tracks.addAll(tracks)
            adapter.notifyDataSetChanged()

            } else {
                with(binding) {
                    progressBar.visibility = View.GONE
                    favoriteList.visibility = View.GONE
                    placeholderImage.visibility = View.VISIBLE
                    placeholderText.visibility = View.VISIBLE
                }
            }
    }

    private fun startPlayer (track: Track) {
        track.isFavorite = true
        val trackToSend = Gson().toJson(track)
        findNavController().navigate(
            R.id.action_libraryFragment_to_playerFragment, bundleOf(TRACK_KEY to trackToSend)
        )
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(CLICK_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }
}