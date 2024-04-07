package com.example.musicplayer.di

import com.example.musicplayer.di.network.Repository
import com.example.musicplayer.di.network.RepositoryImpl
import com.example.musicplayer.remote.ApiService

fun createRepository(apiService: ApiService): Repository {
    return RepositoryImpl(apiService)
}