package com.example.playlistmaker.ui.search.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.player.models.Track

class TrackViewHolder(parent: ViewGroup): RecyclerView.ViewHolder(LayoutInflater.from(parent.context).inflate(
    R.layout.track, parent, false)) {

    private val albumImage: ImageView = itemView.findViewById(R.id.albumImage)
    private val trackName: TextView = itemView.findViewById(R.id.trackName)
    private val artistName: TextView = itemView.findViewById(R.id.artistName)
    private val trackTime: TextView = itemView.findViewById(R.id.trackTime)

    fun bind (item: Track) {
        trackName.text = item.trackName
        artistName.text = item.artistName
        trackTime.text = item.formatTime(item.trackTimeMillis)

        Glide.with(itemView)
            .load(item.artworkUrl100)
            .placeholder(R.drawable.ic_placeholder)
            .centerCrop()
            .transform(RoundedCorners(2))
            .into(albumImage)

    }

}