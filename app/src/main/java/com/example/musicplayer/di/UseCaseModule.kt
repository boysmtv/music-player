package com.example.musicplayer.di

import com.example.musicplayer.activity.music.usecase.MusicUC
import com.example.musicplayer.di.network.Repository

fun getMusicUC(repository: Repository): MusicUC {
    return MusicUC(repository)
}