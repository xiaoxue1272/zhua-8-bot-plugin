package io.tiangou.engine

interface Work<R> {

    /**
     * 判断任务是否执行中
     */
    fun isWorking(): Boolean

    /**
     * 任务生命周期是否结束
     * 与当前任务是否执行完成无关
     * 若未结束,则为false
     * 可以理解为,有多个任务,生命周期都是同一个
     */
    fun isLifeCycleFinish(): Boolean

    /**
     * 任务类型
     * 同步
     * 异步
     */
    fun workType() : WorkType

    /**
     * 任务的执行结果,如果是同步执行,则一般情况下不会为空
     * 除非说是任务失败或异常且未做结果处理
     * 或异步任务的情况下,未执行完成(包含当前线程同步执行,另外有其他线程来获取结果)
     */
    fun getResult() : R?

    enum class WorkType {
        SYNC,
        ASYNC,
    }

}