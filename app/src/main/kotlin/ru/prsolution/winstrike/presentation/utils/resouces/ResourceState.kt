package ru.prsolution.winstrike.presentation.utils.resouces

sealed class ResourceState {
    object LOADING : ResourceState()
    object SUCCESS : ResourceState()
    object ERROR : ResourceState()
}
