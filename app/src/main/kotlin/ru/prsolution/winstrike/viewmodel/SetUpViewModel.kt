package ru.prsolution.winstrike.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import ru.prsolution.winstrike.data.repository.resouces.Resource
import ru.prsolution.winstrike.domain.models.ArenaSchema
import ru.prsolution.winstrike.domain.usecases.ArenaUseCase
import ru.prsolution.winstrike.presentation.model.ArenaItem
import ru.prsolution.winstrike.presentation.model.SchemaItem
import ru.prsolution.winstrike.presentation.model.mapToPresentation
import ru.prsolution.winstrike.presentation.utils.SingleLiveEvent
import kotlin.coroutines.CoroutineContext

/**
 * Created by Oleg Sitnikov on 2019-02-12
 */


class SetUpViewModel constructor(val arenaUseCase: ArenaUseCase) : ViewModel() {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    // Выбранная  арена по времени и дате
    val arenaSchema = SingleLiveEvent<SchemaItem?>()

    fun fetchSchema(arenaPid: String?, time: Map<String, String>) {
        scope.launch {
            val schema = arenaUseCase.get(arenaPid, time, refresh = true)
            arenaSchema.postValue(schema?.mapToPresentation())
        }
    }


    private fun cancelAllRequests() = coroutineContext.cancel()

    override fun onCleared() {
        super.onCleared()
        cancelAllRequests()
    }

}