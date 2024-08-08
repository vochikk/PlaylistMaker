package com.example.playlistmaker.ui.library.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.data.db.entity.PlayListEntity
import com.example.playlistmaker.databinding.FragmentPlaylistBinding
import com.example.playlistmaker.ui.library.view_holder.PlayListAdapter
import com.example.playlistmaker.ui.library.view_model.PlaylistViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class PlaylistFragment: Fragment() {

    private var _binding: FragmentPlaylistBinding? = null
    private val binding get(): FragmentPlaylistBinding = _binding!!

    private val viewModel: PlaylistViewModel by viewModel()
    private val adapter=  PlayListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvPlayList.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvPlayList.adapter = adapter

        viewModel.stateLiveData.observe(viewLifecycleOwner){
            list -> render(list)
        }

        binding.buttonNewPlaylist.setOnClickListener {
            findNavController().navigate(
                R.id.action_libraryFragment_to_createPlaylistFragment, bundleOf()
            )
        }
    }

    override fun onStart() {
        super.onStart()
        viewModel.get()
    }

    override fun onResume() {
        super.onResume()
        viewModel.get()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun render(list: List<PlayListEntity>) {
        if (list.isEmpty()) {
            with(binding) {
                placeholderImage.visibility = View.VISIBLE
                placeholderText.visibility = View.VISIBLE
            }
        } else {
            binding.placeholderImage.visibility = View.GONE
            binding.placeholderText.visibility = View.GONE
            binding.rvPlayList.visibility = View.VISIBLE
            adapter.list.clear()
            adapter.list.addAll(list)
            adapter.notifyDataSetChanged()
        }
    }
}