package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.FriendMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KClass

object FriendMessageEventLogic : AbstractEventLogic<FriendMessageEvent>("好友消息") {

    override suspend fun logic(event: FriendMessageEvent) : Message {
        logger.info("接收到好友:[${event.friend.nick}]发送的消息:[${event.message.content}]")
        return MessageChainBuilder().append("干啥").build();
    }

    override fun getEventClass(): KClass<FriendMessageEvent> = FriendMessageEvent::class

    override fun getContact(event: FriendMessageEvent): Contact = event.friend
}