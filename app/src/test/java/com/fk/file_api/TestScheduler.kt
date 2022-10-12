package com.fk.file_api

import com.fk.file_api.util.AppSchedulers
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers

class TestScheduler : AppSchedulers {
    override val main: Scheduler
        get() = Schedulers.trampoline()
    override val io: Scheduler
        get() = Schedulers.trampoline()
}