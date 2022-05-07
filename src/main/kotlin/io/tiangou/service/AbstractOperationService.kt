package io.tiangou.service

import io.tiangou.data.Zhua8MessageInfo
import net.mamoe.mirai.utils.MiraiLogger

/**
 * 操作实现抽象层service
 * 注:目前所有操作都是基于MessageEvent 也就是消息事件,因为默认所有操作都是通过消息来触发的
 * 扩展性待考虑,后期是否优化待考虑
 */
abstract class AbstractOperationService (
    protected val messageInfo: Zhua8MessageInfo
): OperationService {

    protected val logger: MiraiLogger = MiraiLogger.Factory.create(this::class)


    override fun doOperator() : List<String> {
        return checkUserPermission(messageInfo)
            .init()
            .execute()
    }

    /**
     * 初始化运行环境,或者说,执行前需要的准备工作
     */
    abstract fun init() : AbstractOperationService

    /**
     * 执行
     * @return 返回给用户的消息,执行结果或别的啥消息内容
     */
    abstract fun execute() : List<String>

    /**
     * 权限校验
     * 默认为不进行校验 如talk指令(对话行为)
     * 若需要进行权限校验,则重写该方法
     * @param messageInfo zhua8机器人消息对象
     * @throws
     */
    open fun checkUserPermission(messageInfo: Zhua8MessageInfo) : AbstractOperationService =
        apply {
            logger.info("当前事件不进行权限校验,消息来源用户:[${messageInfo.sender.nick}]")
        }

}