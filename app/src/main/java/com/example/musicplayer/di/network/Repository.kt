package com.example.musicplayer.di.network

import com.example.musicplayer.activity.music.model.MusicModel
import com.example.musicplayer.activity.music.model.MusicReqModel

interface Repository {

    suspend fun getMusic(model: MusicReqModel): MusicModel

}