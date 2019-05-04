package com.gangozero.prague.app.core

interface UseCase<T> {
    fun get(): T
}