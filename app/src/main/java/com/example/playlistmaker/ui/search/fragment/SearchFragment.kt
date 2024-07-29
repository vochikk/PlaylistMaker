package com.example.playlistmaker.ui.search.fragment

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.player.models.Track
import com.example.playlistmaker.ui.search.view_holder.TrackAdapter
import com.example.playlistmaker.ui.search.state.SearchState
import com.example.playlistmaker.ui.search.state.StatusVisability
import com.example.playlistmaker.ui.search.view_model.SearchViewModel
import com.google.gson.Gson
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get(): FragmentSearchBinding = _binding!!
    private lateinit var textWatcher: TextWatcher

    private var textEditText = ""
    private var isClickAllowed = true
    private lateinit var tracks: List<Track>
    private val adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private val viewModel by viewModel<SearchViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.searchStateLiveData.observe(viewLifecycleOwner) { state ->
            render(state)
        }

        tracks = viewModel.getTracksList()
        setViewElement()

        showViewOnScreen(StatusVisability.HISTORY_VISIBLE)


        binding.buttonClear.setOnClickListener {
            clearInputEditText(it)
            updateAdapterAfterClear(emptyList())
            binding.placeholder.visibility = View.GONE
            setViewElement()
        }

        binding.inputEditText.setOnFocusChangeListener { v, hasFocus ->
            binding.historySearch.visibility =
                if (binding.root.hasFocus() && binding.inputEditText.text?.isEmpty() == true)
                    View.VISIBLE else View.GONE
            binding.placeholder.visibility = View.GONE
            setViewElement()
        }

        textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchDebounce(
                    s?.toString() ?: ""
                )
                binding.buttonClear.visibility = clearButtonVisibility(s)
                textEditText = binding.inputEditText.text.toString()
                binding.historySearch.visibility =
                    if (binding.inputEditText.hasFocus() && s?.isEmpty() == true)
                        View.VISIBLE else View.GONE
                binding.placeholder.visibility = View.GONE
                setViewElement()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        textWatcher?.let { binding.inputEditText.addTextChangedListener(it) }


        adapter.tracks = tracks as ArrayList<Track>
        binding.rvSearchList.adapter = adapter
        adapter.setOnClickListener(object : TrackAdapter.OnClickListener {
            override fun onClick(track: Track) {
                viewModel.updateFavoriteTag(track)
                if (clickDebounce()) {
                    startPlayer(track)
                }
            }
        })

        historyAdapter.tracks.addAll(viewModel.getTracksList())
        binding.rvHistorySearch.adapter = historyAdapter
        historyAdapter.setOnClickListener(object : TrackAdapter.OnClickListener {
            override fun onClick(track: Track) {
                viewModel.updateFavoriteTag(track)
                if (clickDebounce()) {
                    startPlayer(track)
                }
            }
        })

        binding.buttonUpdate.setOnClickListener {
            viewModel.searchRequest(textEditText)
        }

        binding.buttonRemove.setOnClickListener {
            viewModel.clearTracksList()
            updateAdapterAfterClear(emptyList())
            setViewElement()
        }
    }

    override fun onResume() {
        super.onResume()
        isClickAllowed = true
    }

    override fun onDestroy() {
        super.onDestroy()
        textWatcher?.let { binding.inputEditText.removeTextChangedListener(it) }
        _binding = null
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun View.hideKeyboard() {
        val inputMethodManager =
            requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
        showViewOnScreen(StatusVisability.HISTORY_VISIBLE)
    }

    private fun render(state: SearchState) {
        when (state) {
            is SearchState.isLoading -> showViewOnScreen(StatusVisability.SHOW_LOAD)
            is SearchState.Error -> showViewOnScreen(StatusVisability.ERROR_VISIBLE)
            is SearchState.NonFound -> showViewOnScreen(StatusVisability.NON_FOUND_VISIBLE)
            is SearchState.Content -> {
                showViewOnScreen(StatusVisability.SEARCH_VISIBLE)
                updateAdapterAfterClear(state.data)
            }

            is SearchState.HistoryView -> {
                showViewOnScreen(StatusVisability.HISTORY_VISIBLE)
                updateAdapterAfterClear(viewModel.getTracksList())
                setViewElement()
            }
        }
    }

    private fun showViewOnScreen(status: StatusVisability) {
        when (status) {
            StatusVisability.NON_FOUND_VISIBLE -> {
                adapter.notifyDataSetChanged()
                binding.rvSearchList.visibility = View.GONE
                binding.historySearch.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.ic_nothing_found)
                binding.placeholderText.setText(R.string.nothing_found)
                binding.buttonUpdate.visibility = View.GONE

            }

            StatusVisability.ERROR_VISIBLE -> {
                adapter.notifyDataSetChanged()
                binding.rvSearchList.visibility = View.GONE
                binding.historySearch.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.ic_failure)
                binding.placeholderText.setText(R.string.failure_text)
                binding.buttonUpdate.visibility = View.VISIBLE
            }

            StatusVisability.SEARCH_VISIBLE -> {
                binding.placeholder.visibility = View.GONE
                binding.historySearch.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                binding.rvSearchList.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            }

            StatusVisability.HISTORY_VISIBLE -> {
                binding.placeholder.visibility = View.GONE
                binding.historySearch.visibility = View.VISIBLE
                binding.rvSearchList.visibility = View.GONE
                adapter.notifyDataSetChanged()
            }

            StatusVisability.SHOW_LOAD -> {
                binding.placeholder.visibility = View.GONE
                binding.historySearch.visibility = View.GONE
                binding.rvSearchList.visibility = View.GONE
                binding.progressBar.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun setViewElement() {
        viewTitleHistory()
        viewButtonRemove()
    }

    private fun viewButtonRemove() {
        binding.buttonRemove.visibility =
            if (viewModel.getTracksList().isNotEmpty() && binding.historySearch.isVisible)
                View.VISIBLE else View.GONE

    }

    private fun viewTitleHistory() {
        binding.titleHistory.visibility =
            if (viewModel.getTracksList().isNotEmpty() && binding.historySearch.isVisible)
                View.VISIBLE else View.GONE
    }

    private fun updateAdapterAfterClear(trackList: List<Track>) {
        adapter.tracks.clear()
        adapter.tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(viewModel.getTracksList())
        historyAdapter.notifyDataSetChanged()
    }

    private fun clearInputEditText(view: View) {
        binding.inputEditText.setText("")
        view.hideKeyboard()
    }

    private fun startPlayer(track: Track) {
        val trackToSend = Gson().toJson(track)
        findNavController().navigate(
            R.id.action_searchFragment_to_playerFragment, bundleOf(TRACK_KEY to trackToSend)
        )
    }

    private fun clickDebounce(): Boolean {
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

    companion object {
        const val TRACK_KEY = "TRACK_KEY"
        const val CLICK_DEBOUNCE_DELAY_MILLIS = 1000L
    }
}