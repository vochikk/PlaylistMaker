package com.example.playlistmaker.ui.player.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.doOnNextLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlayerBinding
import com.example.playlistmaker.domain.library.model.PlayList
import com.example.playlistmaker.domain.player.state.PlayerState
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.ui.player.view_holder.AddPlayListAdapter
import com.example.playlistmaker.ui.player.view_model.PlayerViewModel
import com.example.playlistmaker.ui.search.fragment.SearchFragment.Companion.TRACK_KEY
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel


class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get(): FragmentPlayerBinding = _binding!!

    private val viewModel by viewModel<PlayerViewModel>()
    private var newPlayerState: PlayerState? = null
    private lateinit var adapter: AddPlayListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val trackToView = requireArguments().getString(TRACK_KEY)
        val track = Gson().fromJson(trackToView, Track::class.java)
        adapter = AddPlayListAdapter(view.context)
        binding.rvPlayList.adapter = adapter

        val bottomSheetBehavior: BottomSheetBehavior<LinearLayout> = BottomSheetBehavior.from(binding.bottomSheet).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        binding.root.doOnNextLayout {
            bottomSheetBehavior.peekHeight = binding.playerFragment.height - binding.buttonAdd.bottom
        }
        
        viewModel.playerStateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }
        viewModel.likeStateLiveData.observe(viewLifecycleOwner) { state ->
            setLikeButton(track.isFavorite)
        }

        drawLayout(track)
        viewModel.prepare(track)

        binding.buttonBack.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.buttonPlay.setOnClickListener {
            if (newPlayerState!!.isButtonPlay) {
                viewModel.play()
            } else {
                viewModel.pause()
            }
        }

        binding.buttonLike.setOnClickListener {
            viewModel.updateFavorite(track)
        }

        binding.buttonAdd.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }

        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            viewModel.getPlayList()
                            viewModel.playlistLiveData.observe(viewLifecycleOwner) { list ->
                                renderBottomSheet(list)
                            }

                            adapter.setOnClickListener(object : AddPlayListAdapter.OnClickListener {
                                override fun onClick(playList: PlayList) {
                                    viewModel.updatePlayList(track, playList, requireContext())
                                    viewModel.getPlayList()
                                }
                            })
                            binding.buttonNewPlaylist.setOnClickListener {
                                findNavController()
                                    .navigate(
                                        R.id.action_playerFragment_to_createPlaylistFragment,
                                        bundleOf()
                                    )
                            }
                        }

                        else -> {}
                    }
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            })

    }

    override fun onPause() {
        super.onPause()
        viewModel.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.realese()
        _binding = null
    }

    private fun render(state: PlayerState) {
        setButton(state.isButtonPlay)
        setTimerText(state.progress)
        newPlayerState = state
    }

    private fun drawLayout(track: Track) {

        binding.trackName.text = track.trackName
        binding.artistName.text = track.artistName
        binding.trackTime.text = track.formatTime(track.trackTimeMillis)
        binding.releaseDate.text = track.getReleaseYear()
        binding.genreName.text = track.primaryGenreName
        binding.country.text = track.country
        setLikeButton(track.isFavorite)

        val itemUrl = track.getCoverArtwork()
        Glide.with(this)
            .load(itemUrl)
            .placeholder(R.drawable.ic_placeholder_312x312)
            .transform(RoundedCorners(24))
            .into(binding.imageAlbum)

        if (track.collectionName?.isEmpty() == null) {
            binding.albumGroup.visibility = View.GONE
        } else {
            binding.albumName.text = track.collectionName
        }
    }

    private fun setButton(isButtonPlay: Boolean) {
        if (isButtonPlay) {
            binding.buttonPlay.setImageResource(R.drawable.ic_button_play)
        } else {
            binding.buttonPlay.setImageResource(R.drawable.ic_button_pause)
        }
    }

    private fun setLikeButton(isFavorite: Boolean) {
        if (isFavorite) {
            binding.buttonLike.setImageResource(R.drawable.ic_button_like_active)
        } else {
            binding.buttonLike.setImageResource(R.drawable.ic_button_like)
        }
    }

    private fun setTimerText(progress: String) {
        binding.playTime.text = progress
    }

    private fun renderBottomSheet(list: List<PlayList>) {
        if (!list.isNullOrEmpty()) {
            adapter.playList.clear()
            adapter.playList.addAll(list)
            adapter.notifyDataSetChanged()
            binding.rvPlayList.visibility = View.VISIBLE
        }
    }

    private fun seeMessage(playList: PlayList, isInclude: Boolean) {
        val str = if (isInclude) {
            "${
                view?.resources
                    ?.getString(
                        R.string.track_in_playlist
                    )
            } ${playList.namePlaylist}"
        } else {
            "${
                view?.resources
                    ?.getString(
                        R.string.track_add_in_playlist
                    )
            } ${playList.namePlaylist}"
        }
        val toast: Toast = Toast.makeText(requireContext(), str, Toast.LENGTH_SHORT)
        toast.show()
    }
}