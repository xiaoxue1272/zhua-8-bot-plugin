package io.tiangou.threadpool

import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object CommonThreadPool : ThreadPoolExecutor(
    1,
    Runtime.getRuntime().availableProcessors(),
    30,
    TimeUnit.SECONDS,
    LinkedBlockingQueue()
)