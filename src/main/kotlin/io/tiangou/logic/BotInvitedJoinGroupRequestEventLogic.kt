package io.tiangou.logic

import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import kotlin.reflect.KClass

class BotInvitedJoinGroupRequestEventLogic : AbstractEventLogic<BotInvitedJoinGroupRequestEvent>("群邀请") {

    override suspend fun logic(event: BotInvitedJoinGroupRequestEvent) {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        logger.info("监听到入群邀请,邀请人:${event.invitorNick},群号:$event.groupId, 群名称:$event.groupName")
        event.accept()
    }

    override fun getEventClass(): KClass<BotInvitedJoinGroupRequestEvent> {
        return BotInvitedJoinGroupRequestEvent::class
    }
}