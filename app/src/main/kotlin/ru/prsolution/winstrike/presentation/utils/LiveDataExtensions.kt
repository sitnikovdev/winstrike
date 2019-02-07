package ru.prsolution.winstrike.presentation.utils

import androidx.lifecycle.MutableLiveData
import ru.prsolution.winstrike.presentation.utils.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.resouces.ResourceState

fun <T> MutableLiveData<Resource<T>>.setSuccess(data: T) =
    postValue(Resource(
            ResourceState.SUCCESS, data))

fun <T> MutableLiveData<Resource<T>>.setLoading() =
    postValue(Resource(
            ResourceState.LOADING, value?.data))

fun <T> MutableLiveData<Resource<T>>.setError(message: String? = null) =
    postValue(Resource(
            ResourceState.ERROR, value?.data, message))
