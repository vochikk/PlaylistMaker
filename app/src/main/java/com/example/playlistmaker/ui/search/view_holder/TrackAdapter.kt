package com.example.playlistmaker.ui.search.view_holder

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.domain.player.models.Track

class TrackAdapter (): RecyclerView.Adapter<TrackViewHolder> () {
    var tracks = mutableListOf<Track>()
    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder = TrackViewHolder(parent)

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        holder.bind(tracks[position])
        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(tracks[position])
            }
        }
    }

    override fun getItemCount(): Int = tracks.size

    fun setOnClickListener (onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick (track: Track)
    }

}