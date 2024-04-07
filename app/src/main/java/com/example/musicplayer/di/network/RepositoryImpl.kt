package com.example.musicplayer.di.network

import com.example.musicplayer.activity.music.model.MusicModel
import com.example.musicplayer.activity.music.model.MusicReqModel
import com.example.musicplayer.remote.ApiService

class RepositoryImpl (private val apiService: ApiService) : Repository {

    override suspend fun getMusic(model: MusicReqModel): MusicModel {
        return apiService.getMusic(model.term, model.entity)
    }

}