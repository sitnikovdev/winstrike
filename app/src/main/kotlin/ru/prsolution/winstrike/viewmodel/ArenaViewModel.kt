package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.ArenaRepository
import ru.prsolution.winstrike.domain.models.Arena
import ru.prsolution.winstrike.networking.ApiFactory
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */



class ArenaViewModel : ViewModel(){

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val arenaRepository : ArenaRepository = ArenaRepository(ApiFactory.arenaApi)


    // Список имеющихся арен
    val arenaList = SingleLiveEvent<List<Arena>>()

    fun fetchArenas(){
        scope.launch {
            val arenas = arenaRepository.get()
            arenaList.postValue(arenas)
        }
    }


    fun cancelAllRequests() = coroutineContext.cancel()

}