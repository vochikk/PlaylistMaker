package com.example.playlistmaker.ui.library.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.playlistmaker.R
import com.example.playlistmaker.data.db.entity.PlayListEntity

class PlayListAdapter (): RecyclerView.Adapter<PlayListViewHolder>() {

    var list = ArrayList<PlayListEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.playlist, parent, false)
        return PlayListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PlayListViewHolder, position: Int) {
        holder.bind(list[position])
    }
}