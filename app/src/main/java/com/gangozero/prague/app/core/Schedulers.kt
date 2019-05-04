package com.gangozero.prague.app.core

import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers

class Schedulers {
    fun ui(): Scheduler {
        return AndroidSchedulers.mainThread()
    }

    fun background(): Scheduler {
        return io.reactivex.schedulers.Schedulers.io()
    }
}