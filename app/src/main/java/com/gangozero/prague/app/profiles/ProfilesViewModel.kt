package com.gangozero.prague.app.profiles

import com.gangozero.prague.app.core.Schedulers
import com.gangozero.prague.app.core.UseCase
import io.reactivex.Observable

class ProfilesViewModel(
        private val getProfiles: UseCase<List<Profile>>,
        private val schedulers: Schedulers
) {

    fun profiles(): Observable<State> {

        return Observable.concat(
                Observable.just(State.Loading),
                getProfiles().onErrorResumeNext(Observable.just(State.Error))
        )
    }

    private fun getProfiles(): Observable<State> {
        return Observable.fromCallable { State.Success(getProfiles.get()) }
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .cast(State::class.java)
    }

}