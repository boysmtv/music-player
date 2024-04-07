package com.example.musicplayer.activity.music.usecase

import com.example.musicplayer.activity.music.model.MusicModel
import com.example.musicplayer.activity.music.model.MusicReqModel
import com.example.musicplayer.di.network.Repository
import com.example.musicplayer.domain.usecase.UseCase

class MusicUC constructor(
    private val repository: Repository
) : UseCase<MusicModel, Any?>() {

    private val TAG = this::class.java.simpleName

    override suspend fun run(params: Any?): MusicModel {
        return repository.getMusic(params as MusicReqModel)
    }

}