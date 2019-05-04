package com.gangozero.prague.app.profiles

import android.util.Log
import com.gangozero.prague.app.core.Schedulers
import com.gangozero.prague.app.core.UseCase
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.functions.Function

class ProfilesViewModel(
        private val getProfiles: UseCase<Unit, List<Profile>>,
        private val submitGradeCase: UseCase<Boolean, Unit>,
        private val schedulers: Schedulers
) {

    fun submitGrade(
            like: Boolean
    ) {
        Completable.fromAction { submitGradeCase.get(like) }
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .subscribe({
                    Log.d("grade", "ok")
                }, {
                    Log.d("grade", "error")
                    it.printStackTrace()
                })

    }

    fun profiles(): Observable<State> {

        return Observable.concat(
                Observable.just(State.Loading),
                getProfiles().onErrorResumeNext(object : Function<Throwable, Observable<State>> {
                    override fun apply(t: Throwable): Observable<State> {
                        t.printStackTrace()
                        return Observable.just(State.Error(t))
                    }
                })
        )
    }

    private fun getProfiles(): Observable<State> {
        return Observable.fromCallable { State.Success(getProfiles.get(Unit)) }
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .cast(State::class.java)
    }

}