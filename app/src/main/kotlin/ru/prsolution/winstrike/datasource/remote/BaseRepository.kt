package ru.prsolution.winstrike.datasource.remote

import retrofit2.Response
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.data.repository.resouces.ResourceState
import timber.log.Timber

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */

open class BaseRepository{

    suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>, errorMessage: String): Resource<T>? {

        val result : Resource<T> = safeApiResource(call,errorMessage)
//        var data : Resource<T>? = null

        when(result.state) {
            is ResourceState.SUCCESS ->
                Timber.tag("$$$").d( "${result.state}  - ${result.data.toString()}")
            is ResourceState.ERROR -> {
                Timber.tag("$$$").d( "$errorMessage & Exception - ${result.message}")
            }
        }
        return result

    }

    private suspend fun <T: Any> safeApiResource(call: suspend ()-> Response<T>, errorMessage: String) : Resource<T> {
        val response = call.invoke()
        if(response.isSuccessful) return Resource(ResourceState.SUCCESS,response.body())

        return Resource(ResourceState.ERROR,null, "Error Occurred during getting safe Api result, Custom ERROR - $errorMessage")
    }
}

