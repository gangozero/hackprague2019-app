package com.gangozero.prague.app.core

interface UseCase<In, Out> {
    fun get(input: In): Out
}