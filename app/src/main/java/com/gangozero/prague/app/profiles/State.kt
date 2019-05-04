package com.gangozero.prague.app.profiles

sealed class State {
    object Loading : State()
    object Error : State()
    class Success(val profiles: List<Profile>) : State()
}