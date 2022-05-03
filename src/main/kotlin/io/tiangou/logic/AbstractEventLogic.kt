package io.tiangou.logic

import io.tiangou.data.Zhua8MessageInfo
import io.tiangou.enums.OperationEnum
import io.tiangou.expection.Zhua8BotException
import io.tiangou.service.factory.FactoryEnum
import io.tiangou.service.factory.OperationServiceFactory
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.info
import kotlin.reflect.KClass

abstract class AbstractEventLogic<E: Event>(
    private val onListenActionDescription: String
) : EventLogic {

    protected val logger = MiraiLogger.Factory.create(EventLogic::class)

    private lateinit var listener: Listener<E>

    private var isLogicLoad : Boolean = false;

    protected val serviceFactory: OperationServiceFactory = OperationServiceFactory.getFactory(FactoryEnum.DEFAULT)

    override fun loadLogic() {
        if (isLogicLoad) {
            logger.warning("zhua8机器人${onListenActionDescription}监听已配置,不重复设置监听行为,结束");
            return
        }
        try {
            logger.info { "开始配置zhua8机器人${onListenActionDescription}监听行为" }
            listener = GlobalEventChannel
                // priority = EventPriority.MONITOR 该选项为设置并发优先级
                .subscribeAlways<E>(eventClass = getEventClass(), priority = EventPriority.MONITOR){
                    doLogic(it)
                }
            isLogicLoad = true
            logger.info{"zhua8机器人${onListenActionDescription}监听行为配置完成"}
        } catch (e: Exception) {
            logger.error("zhua8机器人${onListenActionDescription}监听行为配置异常", e)
        }
    }

    private suspend fun doLogic(event: E) {
        var replyMessage: Message?;
        try {
            replyMessage = logic(event)
        } catch (e : Zhua8BotException) {
            logger.warning("zhua8机器人,[${event::class.simpleName}]事件逻辑执行异常,错误码:[${e.errorCode}],错误信息:[${e.errorMessage}]")
            replyMessage = MessageChainBuilder()
                .append("错误:[${e.errorCode}],错误信息:[${e.errorMessage}]")
                .build()
        }
        // 若回复对象不为空 且 回复消息不为空,则回执消息
        getContact(event)?.apply {
            val contact = this
            replyMessage?.run {
                logger.info("回复事件类型:[${event::class.simpleName}],回复对象:[${contact.id}],回复信息:[${this.content}]")
                contact.sendMessage(addMessageHeader(this, event))
            }
        }
    }

    override fun destroyLogic() {
        if (isLogicLoad) {
            logger.warning("开始卸载zhua8机器人${onListenActionDescription}监听配置");
            listener.complete()
        }
        logger.warning("已卸载zhua8机器人${onListenActionDescription}监听配置");
        isLogicLoad = false
    }

    /**
     * 根据不同的事件类型进行逻辑处理
     * @param event 当前事件
     * @return 逻辑执行后的回执信息
     */
    abstract suspend fun logic(event : E) : Message?

    /**
     * 获取当前事件的消息回执人
     * @param event 当前事件
     * @return 逻辑执行后的回执信息
     */
    abstract fun getContact(event : E): Contact?

    /**
     * 当前事件class类型 用于父类构造消息监听器
     */
    abstract fun getEventClass () : KClass<E>

    open fun addMessageHeader(message: Message, event: E) : Message  = message

    fun stringToMessage(stringMessage: String) : Message =
        MessageChainBuilder().append(stringMessage).build()

    fun judgeMessageOperation(messageInfo: Zhua8MessageInfo): OperationEnum {
        return OperationEnum.getConformTypeEnum(messageInfo.prefix)
    }

    fun convertMessage(message: String, user: User) : Zhua8MessageInfo {
        val maybePrefix: String? = OperationEnum
            .operationRegex
            .find(message)
            ?.value
        var prefix: String? = null
        if (maybePrefix != null && message.startsWith(maybePrefix)) {
            prefix = maybePrefix
        }
        val body : String
        if (prefix != null) {
            body = message.removePrefix(prefix).trimStart()
        } else {
            body = message;
        }
        return Zhua8MessageInfo(prefix, body, user);
    }

    open fun <E : MessageEvent> executeService(event: E) : String {
        val messageInfo = convertMessage(event.message.content, event.sender);
        val operation = judgeMessageOperation(messageInfo)
        logger.info("解析当前消息操作类型为:[${operation.desc}],开始执行对应下游服务逻辑")
        return doService(messageInfo, operation)
    }


    open fun doService(messageInfo: Zhua8MessageInfo, operation: OperationEnum) : String =
        serviceFactory.createOperationService(messageInfo, operation).doOperator()

}