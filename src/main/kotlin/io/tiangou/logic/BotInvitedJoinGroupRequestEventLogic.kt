package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import kotlin.reflect.KClass

class BotInvitedJoinGroupRequestEventLogic : AbstractEventLogic<BotInvitedJoinGroupRequestEvent>("群邀请") {

    override suspend fun logic(event: BotInvitedJoinGroupRequestEvent) : Message {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        logger.info("监听到入群邀请,邀请人:[${event.invitorNick}],群号:[${event.groupId}], 群名称:[${event.groupName}]")
        event.accept()
        return MessageChainBuilder()
            .build()
            .plus("已接受入群邀请,邀请人:[${event.invitorNick}],群号:[${event.groupId}], 群名称:[${event.groupName}]")
    }

    override fun getEventClass(): KClass<BotInvitedJoinGroupRequestEvent> {
        return BotInvitedJoinGroupRequestEvent::class
    }

    override fun getContact(event: BotInvitedJoinGroupRequestEvent): Contact? {
        return event.invitor
    }
}