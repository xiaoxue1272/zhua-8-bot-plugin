package io.tiangou.logic

import io.tiangou.constants.Constants
import io.tiangou.data.Zhua8MessageInfo
import io.tiangou.enums.ErrorCodeEnum
import io.tiangou.enums.OperationEnum
import io.tiangou.enums.SpecialMessageEnum
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
import org.slf4j.LoggerFactory
import kotlin.reflect.KClass

abstract class AbstractEventLogic<E: Event>(
    private val onListenActionDescription: String
) : EventLogic {

    protected val log = LoggerFactory.getLogger(this::class.java)

    private lateinit var listener: Listener<E>

    private var isLogicLoad : Boolean = false

    protected val serviceFactory: OperationServiceFactory = OperationServiceFactory.getFactory(FactoryEnum.DEFAULT)

    override fun loadLogic() {
        if (isLogicLoad) {
            log.warn("zhua8机器人[{}],监听已配置,不重复设置监听行为,结束", onListenActionDescription)
            return
        }
        try {
            log.info("开始配置zhua8机器人[{}]监听行为", onListenActionDescription)
            listener = GlobalEventChannel
                // priority = EventPriority.MONITOR 该选项为设置并发优先级
                .subscribeAlways(eventClass = getEventClass(), priority = EventPriority.MONITOR){
                    doLogic(it)
                }
            isLogicLoad = true
            log.info("zhua8机器人[{}]监听行为配置完成", onListenActionDescription)
        } catch (e: Exception) {
            log.error("zhua8机器人[{}]监听行为配置异常,{}", onListenActionDescription, e.stackTrace)
        }
    }

    private suspend fun doLogic(event: E) {
        var replyMessage: Message?
        try {
            replyMessage = logic(event)
        } catch (e : Exception) {
            var errorCode : String? = null
            var errorMessage : String? = null
            if (e is Zhua8BotException) {
                log.warn("zhua8机器人,[{}]事件逻辑执行异常,错误码:[{}],错误信息:[{}]",
                    event::class.simpleName, e.errorCode, e.errorMessage)
                errorCode = e.errorCode
                errorMessage = e.errorMessage
            }
            log.error("zhua8机器人,[{}]事件逻辑执行系统异常\n{}", event::class.simpleName, e.stackTrace)
            replyMessage = MessageChainBuilder()
                .append("错误:[${errorCode ?: ErrorCodeEnum.SYSTEM_ERROR.errorCode}],错误信息:[${errorMessage ?: ErrorCodeEnum.SYSTEM_ERROR.errorMessage}]")
                .build()
        }
        // 若回复对象不为空 且 回复消息不为空,则回执消息
        getContact(event)?.apply {
            val contact = this
            replyMessage?.run {
                log.info("回复事件类型:[{}],回复对象:[{}],回复信息:[{}]", event::class.simpleName, contact.id, content)
                contact.sendMessage(addMessageHeader(this, event))
            }
        }
    }

    override fun destroyLogic() {
        if (isLogicLoad) {
            log.warn("开始卸载zhua8机器人[{}]监听配置", onListenActionDescription)
            listener.complete()
        }
        log.warn("已卸载zhua8机器人[{}]监听配置", onListenActionDescription)
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
    abstract fun getEventClass () : KClass<out E>

    open fun addMessageHeader(message: Message, event: E) : Message  = message

    fun stringToMessage(vararg stringMessage: String) : Message =
        MessageChainBuilder().apply {
            stringMessage.forEach {
                if (it == Constants.EMPTY_STRING) {
                    append("?")
                } else{
                    append(it + "\n")
                }
            }
            if (stringMessage.isEmpty()) {
                append("?")
            }
        }.build()

    fun stringToMessage(stringMessage: List<String>) : Message =
        MessageChainBuilder().apply {
            if (stringMessage.isEmpty()) {
                append("?")
            }
            stringMessage.forEach {
                if (it != Constants.EMPTY_STRING) {
                    append(it + "\n")
                }
            }
        }.build()

    fun judgeMessageOperation(prefix: String?): OperationEnum {
        return OperationEnum.getConformTypeEnum(prefix)
    }

    fun convertMessage(message: String, sender: User) : Zhua8MessageInfo {
        var body : String? = message
        return Zhua8MessageInfo.Builder()
            .prefix(
                Constants.OPERATION_REGEX
                    .find(message)
                    ?.value?.let {
                        if (message.startsWith(it)) {
                            body = message.removePrefix(it).trimStart()
                            it
                        } else {
                            null
                        }
                    }
            )
            .body(body)
            .sender(sender)
            .build()
    }

    open fun <E : MessageEvent> executeService(event: E) : List<String> {
        val zhua8MessageInfo =  event.run {
            SpecialMessageEnum.getMessageInfoIfSpecial(message.content, sender)
                ?: convertMessage(message.content, sender)
        }
        val operation = judgeMessageOperation(zhua8MessageInfo.prefix)
        log.info("解析当前消息操作类型为:[{}],开始执行对应下游服务逻辑", operation.desc)
        doService(zhua8MessageInfo, operation).let {
            return zhua8MessageInfo.specifyReplyMessages ?: it
        }
    }



    open fun doService(messageInfo: Zhua8MessageInfo, operation: OperationEnum) : List<String> =
        serviceFactory.createOperationService(messageInfo, operation).doOperator()

}