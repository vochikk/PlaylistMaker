package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.view.isVisible
import com.example.playlistmaker.databinding.ActivitySearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

const val TRACK_LIST_HISTORY = "track_list_history"
const val TEXT_KEY = "TEXT_KEY"

class SearchActivity : AppCompatActivity() {
    private var textEditText = ""
    private lateinit var binding: ActivitySearchBinding

    private val itunesBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchService = retrofit.create(SearchiTunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()
    private val historyAdapter = TrackAdapter()


    private val callback = object: Callback<TracksResponse> {

        override fun onResponse(
            call: Call<TracksResponse>,
            response: Response<TracksResponse>
        ) {
            if(response.code() == 200) {
                tracks.clear()
                if (response.body()?.results?.isNotEmpty() == true){
                    tracks.addAll(response.body()?.results!!)
                    showSearchResult(SearchStatus.SUCCESS)
                }
                if (tracks.isEmpty()) {
                    showSearchResult(SearchStatus.EMMPTY_SEARCH)
                } else {
                    showSearchResult(SearchStatus.SEARCH_FAILURE)
                }
            } else {
                showSearchResult(SearchStatus.SEARCH_FAILURE)
            }
        }

        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
            tracks.clear()
            showSearchResult(SearchStatus.SEARCH_FAILURE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val sheredPref = getSharedPreferences(TRACK_LIST_HISTORY, MODE_PRIVATE)
        val searchHistory = SearchHistory(sheredPref)
        viewButtonRemove(searchHistory.getTrackList())

        binding.buttonBack.setOnClickListener {
            finish()
        }

        binding.buttonClear.setOnClickListener {
            binding.inputEditText.setText("")
            it.hideKeyboard()
            tracks.clear()
            adapter.notifyDataSetChanged()
            historyAdapter.tracks.clear()
            historyAdapter.tracks.addAll(searchHistory.getTrackList())
            historyAdapter.notifyDataSetChanged()
            binding.placeholder.visibility = View.GONE
            viewButtonRemove(searchHistory.getTrackList())
        }

        binding.inputEditText.setOnFocusChangeListener { v, hasFocus ->
            binding.historySearch.visibility = if (binding.root.hasFocus() && binding.inputEditText.text?.isEmpty() == true)
                View.VISIBLE else View.GONE
            binding.placeholder.visibility = View.GONE
            viewButtonRemove(searchHistory.getTrackList())
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.buttonClear.visibility = clearButtonVisibility(s)
                textEditText = binding.inputEditText.text.toString()
                binding.historySearch.visibility = if (binding.inputEditText.hasFocus() && s?.isEmpty() == true)
                    View.VISIBLE else View.GONE
                binding.placeholder.visibility = View.GONE
                viewButtonRemove(searchHistory.getTrackList())
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        binding.inputEditText.addTextChangedListener(textWatcher)


        adapter.tracks = tracks
        binding.rvSearchList.adapter = adapter
        adapter.setOnClickListener(object : TrackAdapter.OnClickListener {
            override fun onClick(track: Track) {
                searchHistory.saveTrack(track, searchHistory.getTrackList())
            }
        })

        historyAdapter.tracks.addAll(searchHistory.getTrackList())
        binding.rvHistorySearch.adapter = historyAdapter
        historyAdapter.setOnClickListener(object : TrackAdapter.OnClickListener {
            override fun onClick(track: Track) {
                searchHistory.saveTrack(track, searchHistory.getTrackList())
            }
        })


        binding.inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (binding.inputEditText.text.isNotEmpty()){
                    itunesSearchService.search(binding.inputEditText.text.toString()).enqueue(callback)
                } else {
                    binding.rvSearchList.visibility = View.GONE
                    historyAdapter.tracks.clear()
                    historyAdapter.tracks.addAll(searchHistory.getTrackList())
                    historyAdapter.notifyDataSetChanged()
                    binding.historySearch.visibility = View.VISIBLE
                }
                true
            }
            false
        }

        binding.buttonUpdate.setOnClickListener {
            itunesSearchService.search(binding.inputEditText.text.toString()).enqueue(callback)
        }

        binding.buttonRemove.setOnClickListener {
            searchHistory.removeTrackList()
            historyAdapter.tracks.clear()
            historyAdapter.notifyDataSetChanged()
            binding.buttonRemove.visibility = View.GONE
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
    }

    private fun showSearchResult (searchStatus: SearchStatus) {
        when (searchStatus) {
            SearchStatus.EMMPTY_SEARCH -> {
                adapter.notifyDataSetChanged()
                binding.placeholder.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.ic_nothing_found)
                binding.placeholderText.setText(R.string.nothing_found)
                binding.buttonUpdate.visibility = View.GONE

            }
            SearchStatus.SEARCH_FAILURE -> {
                adapter.notifyDataSetChanged()
                binding.placeholder.visibility = View.VISIBLE
                binding.placeholderImage.setImageResource(R.drawable.ic_failure)
                binding.placeholderText.setText(R.string.failure_text)
                binding.buttonUpdate.visibility = View.VISIBLE
            }
            SearchStatus.SUCCESS -> {
                binding.placeholder.visibility = View.GONE
                binding.rvSearchList.visibility = View.VISIBLE
                adapter.notifyDataSetChanged()
            }
        }
    }

    private fun viewButtonRemove (trackList: ArrayList<Track>) {
        binding.buttonRemove.visibility = if (trackList.isNotEmpty() && binding.historySearch.isVisible)
            View.VISIBLE else View.GONE

    }
}