package ru.prsolution.winstrike.presentation.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import ru.prsolution.winstrike.datasource.model.Room
import ru.prsolution.winstrike.networking.RetrofitFactory
import ru.prsolution.winstrike.presentation.utils.resouces.Resource
import ru.prsolution.winstrike.presentation.utils.setError
import ru.prsolution.winstrike.presentation.utils.setSuccess
import timber.log.Timber

class ArenaListViewModel :
		ViewModel() {

	private val retrofitService = RetrofitFactory.makeRetrofitService()
	val rooms = MutableLiveData<Resource<List<Room>>>()

	fun get() {
		GlobalScope.launch {
			val request = retrofitService.arenas
			try {
				val response = request.await()
				response.body()?.let { rooms.setSuccess(it.rooms) }
			} catch (e: Throwable) {
				rooms.setError(e.message)
				Timber.e(e)
			}
		}
	}

}
