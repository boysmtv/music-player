package com.example.musicplayer.activity.music.presentation

import com.example.musicplayer.activity.music.model.MusicResultModel
import com.example.musicplayer.databinding.ActivitySearchListItemBinding

interface SearchOnClickListener<T> {
    fun onItemClick(position: Int, model: MusicResultModel)
}