package com.gangozero.prague.app.profiles

import com.gangozero.prague.app.core.Schedulers
import com.gangozero.prague.app.core.UseCase
import io.reactivex.Observable
import io.reactivex.functions.Function

class ProfilesViewModel(
        private val getProfiles: UseCase<Unit, List<Profile>>,
        private val submitGradeCase: UseCase<Grade, Unit>,
        private val schedulers: Schedulers
) {

    fun submitGrade(profileId: String, like: Boolean) {
        submitGradeCase.get(Grade(profileId, "", if (like) 1 else -1, 1.0, 1.0))
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

    //Observable.just(State.Error(it))

    private fun getProfiles(): Observable<State> {
        return Observable.fromCallable { State.Success(getProfiles.get(Unit)) }
                .subscribeOn(schedulers.background())
                .observeOn(schedulers.ui())
                .cast(State::class.java)
    }

}