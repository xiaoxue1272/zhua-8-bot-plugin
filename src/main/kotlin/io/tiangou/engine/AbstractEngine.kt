package io.tiangou.engine

import io.tiangou.threadpool.CommonThreadPool

abstract class AbstractEngine<T : Work<*>> : Engine<T> {

    init {
        initEngine()
    }

    abstract fun initEngine()

    override fun execute(work: T) {
        when (work.workType()) {
            Work.WorkType.SYNC -> {
                content(work)
            }
            Work.WorkType.ASYNC -> CommonThreadPool.submit{
                content(work)
            }
        }
    }

    abstract fun content(work: T)

}