package ru.prsolution.winstrike.data.repository

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Exception) : Result<Nothing>()
}

