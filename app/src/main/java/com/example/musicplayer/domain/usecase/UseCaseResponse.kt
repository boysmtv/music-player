package com.example.musicplayer.domain.usecase

import com.example.musicplayer.domain.model.ApiError

interface UseCaseResponse<Type> {

    fun onSuccess(result: Type)

    fun onError(apiError: ApiError)

}

