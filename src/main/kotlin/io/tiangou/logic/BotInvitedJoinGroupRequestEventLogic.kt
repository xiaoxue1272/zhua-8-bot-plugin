package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import kotlin.reflect.KClass

object BotInvitedJoinGroupRequestEventLogic : AbstractEventLogic<BotInvitedJoinGroupRequestEvent>("群邀请") {

    override suspend fun logic(event: BotInvitedJoinGroupRequestEvent) : Message {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        log.info("监听到入群邀请,邀请人:[{}],群号:[{}], 群名称:[{}]", event.invitorNick, event.groupId, event.groupName)
        event.accept()
        return MessageChainBuilder()
            .append("已接受入群邀请,邀请人:[{}],群号:[{}], 群名称:[{}]", event.invitorNick, event.groupId.toString(), event.groupName)
            .build()
    }

    override fun getEventClass(): KClass<BotInvitedJoinGroupRequestEvent> = BotInvitedJoinGroupRequestEvent::class

    override fun getContact(event: BotInvitedJoinGroupRequestEvent): Contact? = event.invitor
}