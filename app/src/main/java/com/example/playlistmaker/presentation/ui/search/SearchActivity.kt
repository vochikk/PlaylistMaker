package com.example.playlistmaker.presentation.ui.search

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.example.playlistmaker.Creator
import com.example.playlistmaker.presentation.ui.player.PlayerActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySearchBinding
import com.example.playlistmaker.domain.api.TracksInteractor
import com.example.playlistmaker.domain.models.Track
import com.example.playlistmaker.presentation.StatusVisability
import com.google.gson.Gson

const val TRACK_KEY = "track_key"

class SearchActivity : AppCompatActivity() {
    private var textEditText = ""
    private lateinit var binding: ActivitySearchBinding
    lateinit var interactor: TracksInteractor

    private var isClickAllowed = true
    lateinit var tracks: List<Track>
    private val adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()
    private val searchRunnable = Runnable { searchRequest() }
    private val handler = Handler (Looper.getMainLooper())



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        interactor = Creator.providesTracksInteractor(this)
        tracks = interactor.getTracksList()
        viewButtonRemove()

        showViewOnScreen(StatusVisability.HISTORY_VISIBLE)

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonClear.setOnClickListener {
            clearInputEditText(it)
            updateAdapterAfterClear(emptyList())
            binding.placeholder.visibility = View.GONE
            viewButtonRemove()
        }

        binding.inputEditText.setOnFocusChangeListener { v, hasFocus ->
            binding.historySearch.visibility = if (binding.root.hasFocus() && binding.inputEditText.text?.isEmpty() == true)
                View.VISIBLE else View.GONE
            binding.placeholder.visibility = View.GONE
            viewButtonRemove()
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                searchDebounce()
                binding.buttonClear.visibility = clearButtonVisibility(s)
                textEditText = binding.inputEditText.text.toString()
                binding.historySearch.visibility = if (binding.inputEditText.hasFocus() && s?.isEmpty() == true)
                    View.VISIBLE else View.GONE
                binding.placeholder.visibility = View.GONE
                viewButtonRemove()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.inputEditText.addTextChangedListener(textWatcher)


        adapter.tracks = tracks as ArrayList<Track>
        binding.rvSearchList.adapter = adapter
        adapter.setOnClickListener(object : TrackAdapter.OnClickListener {
            override fun onClick(track: Track) {
                interactor.saveTrack(track)
                if (clickDebounce()) {
                    startPlayer(track)
                }
            }
        })

        historyAdapter.tracks.addAll(interactor.getTracksList())
        binding.rvHistorySearch.adapter = historyAdapter
        historyAdapter.setOnClickListener(object : TrackAdapter.OnClickListener {
            override fun onClick(track: Track) {
                interactor.saveTrack(track)
                if (clickDebounce()) {
                    startPlayer(track)
                }
            }
        })

        binding.buttonUpdate.setOnClickListener {
            searchRequest()
        }

        binding.buttonRemove.setOnClickListener {
            interactor.clearTracksList()
            updateAdapterAfterClear(emptyList())
            viewButtonRemove()
        }


    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_KEY, textEditText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textEditText = savedInstanceState.getString(TEXT_KEY, "")
        binding.inputEditText.setText(textEditText)
        searchRequest()
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun View.hideKeyboard () {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(windowToken, 0)
        showViewOnScreen(StatusVisability.HISTORY_VISIBLE)
    }

    private fun searchRequest () {
        if (binding.inputEditText.text.isNotEmpty()) {
            showViewOnScreen(StatusVisability.SHOW_LOAD)

            interactor.searchTracks(binding.inputEditText.text.toString(), consumer =
            object: TracksInteractor.TracksConsumer {
                override fun consume(searchTracks: List<Track>?) {
                    handler.post {
                        binding.progressBar.visibility = View.GONE
                        if (searchTracks == null) {
                            showViewOnScreen(StatusVisability.ERROR_VISIBLE)
                        } else {
                            if (searchTracks!!.isNotEmpty()) {
                                tracks = searchTracks
                                showViewOnScreen(StatusVisability.SEARCH_VISIBLE)
                            } else {
                                showViewOnScreen(StatusVisability.NON_FOUND_VISIBLE)
                            }
                            updateAdapterAfterClear(searchTracks)
                        }
                    }
                }
            }
            )
        }
    }

    private fun showViewOnScreen (status: StatusVisability) {
        when (status) {
            StatusVisability.NON_FOUND_VISIBLE -> {
                adapter.notifyDataSetChanged()
                binding.rvSearchList.visibility = View.GONE
                binding.historySearch.visibility = View.GONE
                binding.placeholder.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.ic_nothing_found)
                binding.placeholderText.setText(R.string.nothing_found)
                binding.buttonUpdate.visibility = View.GONE

            }
            StatusVisability.ERROR_VISIBLE -> {
                adapter.notifyDataSetChanged()
                binding.rvSearchList.visibility = View.GONE
                binding.historySearch.visibility = View.GONE
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

    private fun viewButtonRemove () {
        binding.buttonRemove.visibility = if (interactor.getTracksList().isNotEmpty() && binding.historySearch.isVisible)
            View.VISIBLE else View.GONE

    }

    private fun updateAdapterAfterClear (trackList: List<Track>) {
        adapter.tracks.clear()
        adapter.tracks.addAll(trackList)
        adapter.notifyDataSetChanged()
        historyAdapter.tracks.clear()
        historyAdapter.tracks.addAll(interactor.getTracksList())
        historyAdapter.notifyDataSetChanged()
    }

    private fun clearInputEditText (view: View) {
        binding.inputEditText.setText("")
        view.hideKeyboard()
    }

    private fun startPlayer (track: Track) {
        val intent = Intent(this@SearchActivity, PlayerActivity::class.java)
        val trackToSend = Gson().toJson(track)
        intent.putExtra(TRACK_KEY, trackToSend)
        startActivity(intent)
    }

    private fun searchDebounce () {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun clickDebounce() : Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    companion object {
        const val TEXT_KEY = "TEXT_KEY"
        const val SEARCH_DEBOUNCE_DELAY = 2000L
        const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}