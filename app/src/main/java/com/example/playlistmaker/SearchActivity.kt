package com.example.playlistmaker

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Locale


class SearchActivity : AppCompatActivity() {
    private var textEditText = ""
    lateinit var backButton: ImageView
    lateinit var inputEditText: EditText
    lateinit var buttonClear: ImageView
    lateinit var rvSearchList: RecyclerView
    lateinit var nothingPlaceholder: LinearLayout
    lateinit var failurePlaceholder: LinearLayout
    lateinit var buttonUpdate: Button

    private val itunesBaseUrl = "https://itunes.apple.com/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val itunesSearchService = retrofit.create(SearchiTunesApi::class.java)

    private val tracks = ArrayList<Track>()
    private val adapter = TrackAdapter()

    private val callback = object: Callback<TracksResponse> {
        override fun onResponse(
            call: Call<TracksResponse>,
            response: Response<TracksResponse>
        ) {
            if(response.code() == 200) {
                tracks.clear()
                if (response.body()?.results?.isNotEmpty() == true){
                    tracks.addAll(response.body()?.results!!)
                    adapter.notifyDataSetChanged()
                }
                if (tracks.isEmpty()) {
                    showNothingPlaceholder()
                } else {
                    showFailurePlaceholder()
                }
            } else {
                showFailurePlaceholder()
            }
        }

        override fun onFailure(call: Call<TracksResponse>, t: Throwable) {
            showFailurePlaceholder()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        backButton = findViewById(R.id.buttonBack)
        backButton.setOnClickListener {
            finish()
        }

        inputEditText = findViewById(R.id.inputEditText)
        buttonClear = findViewById(R.id.buttonClear)
        rvSearchList = findViewById(R.id.rvSearchList)
        nothingPlaceholder = findViewById(R.id.nothingPlaceholder)
        failurePlaceholder = findViewById(R.id.failurePlaceholder)
        buttonUpdate = findViewById(R.id.buttonUpdate)

        buttonClear.setOnClickListener {
            inputEditText.setText("")
            it.hideKeyboard()
            tracks.clear()
            adapter.notifyDataSetChanged()
            failurePlaceholder.visibility = View.GONE
            nothingPlaceholder.visibility = View.GONE
        }

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                buttonClear.visibility = clearButtonVisibility(s)
                textEditText = inputEditText.text.toString()
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        inputEditText.addTextChangedListener(textWatcher)

        adapter.tracks = tracks
        rvSearchList.adapter = adapter

        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {


                if (inputEditText.text.isNotEmpty()){
                    itunesSearchService.search(inputEditText.text.toString()).enqueue(callback)
                }
                true
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(TEXT_KEY, textEditText)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        textEditText = savedInstanceState.getString(TEXT_KEY, "")
        inputEditText.setText(textEditText)
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

    private fun showNothingPlaceholder() {
        nothingPlaceholder.visibility = View.VISIBLE
    }

    private fun showFailurePlaceholder() {
        failurePlaceholder.visibility = View.VISIBLE
        buttonUpdate.setOnClickListener {
            failurePlaceholder.visibility = View.GONE
            itunesSearchService.search(inputEditText.text.toString()).enqueue(callback)
        }
    }

    private companion object {
        const val TEXT_KEY = "TEXT_KEY"
    }
}