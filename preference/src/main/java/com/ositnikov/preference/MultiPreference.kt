package com.ositnikov.preference

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

@Suppress("UNCHECKED_CAST")
class MultiPreference<T> constructor(updates: Observable<String>,
                                     private val preferences: SharedPreferences,
                                     private val keys: List<String>,
                                     private val defaultValue: T) : MutableLiveData<Pair<String, T>>() {

    private val disposable = CompositeDisposable()

    init {
        disposable.add(updates.filter { t -> keys.contains(t) }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribeWith(object: DisposableObserver<String>() {
                override fun onComplete() {

                }

                override fun onNext(t: String) {
                    postValue(Pair(t, (preferences.all[t] as T) ?: defaultValue))
                }

                override fun onError(e: Throwable) {

                }
            }))
    }
}