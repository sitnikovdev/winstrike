package com.ositnikov.preference

import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

class MultiPreferenceAny constructor(updates: Observable<String>,
                                     private val keys: List<String>) : MutableLiveData<String>() {

    private val disposable = CompositeDisposable()

    init {
        disposable.add(updates.filter { t -> keys.contains(t) }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object: DisposableObserver<String>() {
                override fun onComplete() {

                }

                override fun onNext(t: String) {
                    postValue(t)
                }

                override fun onError(e: Throwable) {

                }
            }))
    }
}