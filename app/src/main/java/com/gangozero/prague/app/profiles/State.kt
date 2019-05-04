package com.gangozero.prague.app.profiles

sealed class State {
    object Loading : State()
    class Error(val throwable: Throwable) : State()
    class Success(val profiles: List<Profile>) : State()
}