package com.gangozero.prague.app.profiles

import com.gangozero.prague.app.core.UseCase
import io.reactivex.Observable

class ProfilesViewModel(
        private val getProfiles: UseCase<List<Profile>>
) {

    fun profiles(): Observable<State> {

        return Observable.just(State.Error)

//        return Observable.concat(
//                { State.Loading },
//                {
//                    Observable.fromCallable {
//                        State.Success(getProfiles.get())
//                    }.error
//                }
//        )
    }

    sealed class State {
        object Loading : State()
        object Error : State()
        class Success(val profiles: List<Profile>) : State()
    }
}