package io.tiangou.engine

open class CommonWork<T, R>(
    /**
     * 任务/工作对象编号
     * 如果多个操作的编号相同,在执行引擎的生命周期里则认为他们是为同一个任务
     */
    val taskNo: String,

    /**
     * 任务执行传递参数
     */
    val parameters: T,

    /**
     * 任务执行传递参数
     */
    private val workType: Work.WorkType,

    private val isLifeCycleFinish: Boolean
) : Work<R> {

    @Volatile
    private var workingFlag = false

    override fun isWorking(): Boolean = workingFlag

    fun changeFlagWorking() {
        workingFlag = true
    }

    override fun isLifeCycleFinish(): Boolean = isLifeCycleFinish

    override fun workType(): Work.WorkType  = workType

    @Volatile
    @JvmField
    var result: R? = null

    override fun getResult(): R? = result
}