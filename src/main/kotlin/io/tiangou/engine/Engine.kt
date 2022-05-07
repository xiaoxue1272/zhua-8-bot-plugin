package io.tiangou.engine

interface Engine<T : Work<*>> {

    fun execute(work: T)

}