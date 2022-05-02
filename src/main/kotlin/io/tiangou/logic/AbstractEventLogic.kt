package io.tiangou.logic

import io.tiangou.Zhua8BotLogic
import io.tiangou.expection.Zhua8BotException
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.info
import java.util.*
import kotlin.reflect.KClass

abstract class AbstractEventLogic<E: Event>(
    private val onListenActionDescription: String
) : EventLogic {

    protected val logger = MiraiLogger.Factory.create(EventLogic::class)

    private lateinit var listener: Listener<E>

    private var isLogicLoad : Boolean = false;

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
                    var replyMessage: Message?;
                    try {
                        replyMessage = logic(it)
                    } catch (e : Zhua8BotException) {
                        logger.warning("zhua8机器人,[${it::class.simpleName}]事件逻辑执行异常", e)
                        replyMessage = MessageChainBuilder().build().plus("错误:[${e.errorCode},错误信息:[${e.errorMessage}]]")
                    }
                    // 若回复对象不为空 且 回复消息不为空,则回执消息
                    val contact = getContact(it);
                    if (replyMessage != null && contact != null) {
                        logger.info("回复事件类型:[${it::class.simpleName}],回复对象:[${contact.id}],回复信息:[${replyMessage.content}]")
                        contact.sendMessage(replyMessage)
                    }
                }
            isLogicLoad = true
            logger.info{"zhua8机器人${onListenActionDescription}监听行为配置完成"}
            return
        } catch (e: Exception) {
            logger.error("zhua8机器人${onListenActionDescription}监听行为配置异常", e)
            return
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

    abstract suspend fun logic(event : E) : Message?

    abstract fun getContact(event : E): Contact?

    abstract fun getEventClass () : KClass<E>

}