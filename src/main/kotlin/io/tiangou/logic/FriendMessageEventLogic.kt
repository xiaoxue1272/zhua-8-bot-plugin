package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KClass

object FriendMessageEventLogic : AbstractEventLogic<FriendMessageEvent>("好友消息") {

    override suspend fun logic(event: FriendMessageEvent) : Message {
        log.info("接收到好友:[{}]发送的消息:[{}]", event.friend.nick, event.message.content)
        return stringToMessage(GroupTempMessageEventLogic.executeService(event))
    }

    override fun getEventClass(): KClass<FriendMessageEvent> = FriendMessageEvent::class

    override fun getContact(event: FriendMessageEvent): Contact = event.friend
}