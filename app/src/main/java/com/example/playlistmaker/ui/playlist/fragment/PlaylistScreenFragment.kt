package com.example.playlistmaker.ui.playlist.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.net.toUri
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistScreenBinding
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.ui.library.fragment.PlaylistFragment.Companion.PLAYLIST
import com.example.playlistmaker.ui.library.view_model.PlaylistViewModel
import com.example.playlistmaker.ui.playlist.view_holder.PlayListScreenAdapter
import com.example.playlistmaker.ui.playlist.view_model.PlaylistScreenViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment.Companion.TRACK_KEY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistScreenFragment : Fragment() {

    private var _binding: FragmentPlaylistScreenBinding? = null
    private val binding get(): FragmentPlaylistScreenBinding = _binding!!

    private val viewModel: PlaylistScreenViewModel by viewModel()
    private val adapter: PlayListScreenAdapter = PlayListScreenAdapter()

    private var name = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaylistScreenBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvTrackList.adapter = adapter
        binding.rvTrackList.visibility = View.VISIBLE
        val playListToView = requireArguments().getInt(PLAYLIST)
        viewModel.getPlayList(playListToView)

        viewModel.playlistLiveData.observe(viewLifecycleOwner) { playlist ->
            drawLayout(playlist)
            name = playlist.namePlaylist
        }

        viewModel.getTrackList(playListToView)

        viewModel.tracklistLiveData.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                binding.emptyPlaylistMessage.visibility = View.VISIBLE
                binding.rvTrackList.visibility = View.GONE
            } else {
                binding.emptyPlaylistMessage.visibility = View.GONE
                binding.rvTrackList.visibility = View.VISIBLE
                adapter.tracks.clear()
                adapter.tracks = list.toMutableList()
                list.forEach {
                    Log.d("sortInView", "${it.timestampToPlaylist}")
                }
                adapter.notifyDataSetChanged()
            }
            viewTotalTime(list)
        }



        adapter.setOnClickListener(object : PlayListScreenAdapter.OnClickListener {
            override fun onClick(track: Track) {
                startPlayer(track)
            }
        })

        adapter.setOnLongClickListener(object : PlayListScreenAdapter.OnLongClickListener {
            override fun onLongClick(track: Track): Boolean {
                MaterialAlertDialogBuilder(requireActivity())
                    .setTitle(view.resources.getString(R.string.delete_dialog))
                    .setNegativeButton(view.resources.getString(R.string.dismiss)) { dialog, which -> }
                    .setPositiveButton(view.resources.getString(R.string.accept)) { dialog, which ->
                        viewModel.deleteTrack(track.trackId, playListToView)
                    }
                    .show()
                return true
            }
        })

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        val bottomSheetListTrack: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(binding.bottomSheetListTrack).apply {
                state = BottomSheetBehavior.STATE_COLLAPSED
            }

        val bottomSheetInMore: BottomSheetBehavior<LinearLayout> =
            BottomSheetBehavior.from(binding.bottomSheetMore).apply {
                state = BottomSheetBehavior.STATE_HIDDEN
            }

        bottomSheetInMore.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            binding.bottomSheetListTrack.visibility = View.GONE
                            binding.buttomShareInMore.setOnClickListener {
                                viewModel.sharingTrack(view.context)
                            }

                            binding.buttonDeleteInMore.setOnClickListener {

                                MaterialAlertDialogBuilder(requireActivity())
                                    .setTitle(
                                        view.resources.getString(
                                            R.string.dialog_delete_playlist,
                                            name
                                        )
                                    )
                                    .setNegativeButton(R.string.dismiss) { dialog, which ->
                                        bottomSheetInMore.state = BottomSheetBehavior.STATE_HIDDEN
                                    }
                                    .setPositiveButton(R.string.accept) { dialog, which ->
                                        viewModel.deletePlaylist(playListToView)
                                        findNavController().navigateUp()
                                    }
                                    .show()

                            }

                            binding.buttonEditInMore.setOnClickListener {
                                findNavController().navigate(
                                    R.id.action_playlistScreenFragment_to_createPlaylistFragment,
                                    bundleOf(
                                        PLAYLIST_TO_EDIT to playListToView
                                    )
                                )
                            }
                        }

                        BottomSheetBehavior.STATE_HIDDEN -> {
                            binding.bottomSheetListTrack.visibility = View.VISIBLE
                        }

                        else -> {}
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            }
        )

        binding.root.doOnNextLayout {
            bottomSheetListTrack.peekHeight =
                binding.playlistScreenView.bottom - binding.buttomMore.bottom
            bottomSheetInMore.peekHeight =
                binding.playlistScreenView.bottom - binding.namePlaylist.bottom
        }

        binding.buttonShare.setOnClickListener {
            viewModel.sharingTrack(view.context)
        }

        binding.buttomMore.setOnClickListener {
            bottomSheetInMore.state = BottomSheetBehavior.STATE_COLLAPSED
        }
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }

    private fun drawLayout(playList: PlayList) {
        binding.namePlaylist.text = playList.namePlaylist
        binding.namePlaylistInMore.text = playList.namePlaylist

        if (playList.about == "") {
            binding.textAbout.visibility = View.GONE
        } else {
            binding.textAbout.visibility = View.VISIBLE
            binding.textAbout.text = playList.about
        }


        if (!playList.imageUri.isNullOrEmpty()) {
            binding.imagePlaylist.setImageURI(playList.imageUri.toUri())
            binding.imagePlaylistInMore.setImageURI(playList.imageUri.toUri())
        }

        binding.sizePlaylist.text = requireContext().resources.getQuantityString(
            R.plurals.plurals,
            playList.sizePlaylist,
            playList.sizePlaylist
        )
        binding.sizePlaylistInMore.text = requireContext().resources.getQuantityString(
            R.plurals.plurals,
            playList.sizePlaylist,
            playList.sizePlaylist
        )
    }

    private fun viewTotalTime(list: List<Track>) {
        var totalTime: Long = 0
        list.forEach {
            totalTime += it.trackTimeMillis
        }

        val timetoview = SimpleDateFormat("mm", Locale.getDefault()).format(totalTime)
        binding.totalTime.text = "${timetoview} ${view?.resources?.getString(R.string.total_time)}"
    }

    private fun startPlayer(track: Track) {
        val trackToSend = Gson().toJson(track)
        findNavController().navigate(
            R.id.action_playlistScreenFragment_to_playerFragment, bundleOf(TRACK_KEY to trackToSend)
        )
    }

    companion object {
        const val PLAYLIST_TO_EDIT = "playlist_to_edit"
    }
}