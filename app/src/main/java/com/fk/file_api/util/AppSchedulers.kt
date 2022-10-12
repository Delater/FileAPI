package com.fk.file_api.util

import io.reactivex.Scheduler

interface AppSchedulers {
    val main: Scheduler
    val io: Scheduler
}