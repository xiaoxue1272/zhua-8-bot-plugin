package io.tiangou.logic

import io.tiangou.Zhua8BotLogic
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.EventPriority
import net.mamoe.mirai.event.GlobalEventChannel
import net.mamoe.mirai.event.Listener
import net.mamoe.mirai.utils.MiraiLogger
import net.mamoe.mirai.utils.info
import kotlin.reflect.KClass

abstract class AbstractEventLogic<E: Event>(
    private val onListenActionDescription: String
) : EventLogic {

    val logger = MiraiLogger.Factory.create(EventLogic::class)

    private lateinit var listener: Listener<E>

    private var isLogicLoad : Boolean = false;

    override fun loadLogic() {
        if (isLogicLoad) {
            logger.warning("zhua8机器人${onListenActionDescription}监听已配置,不重复设置监听行为,结束");
            return
        }
        try {
            Zhua8BotLogic.logger.info { "开始配置zhua8机器人${onListenActionDescription}监听行为" }
            listener = GlobalEventChannel
                // priority = EventPriority.MONITOR 该选项为设置并发优先级
                .subscribeAlways<E>(eventClass = getEventClass(), priority = EventPriority.MONITOR){
                    logic(it);
                }
            isLogicLoad = true
            Zhua8BotLogic.logger.info{"zhua8机器人${onListenActionDescription}监听行为配置完成"}
            return
        } catch (e: Exception) {
            Zhua8BotLogic.logger.error("zhua8机器人${onListenActionDescription}监听行为配置异常", e)
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

    abstract suspend fun logic(event : E)

    abstract fun getEventClass () : KClass<E>

}