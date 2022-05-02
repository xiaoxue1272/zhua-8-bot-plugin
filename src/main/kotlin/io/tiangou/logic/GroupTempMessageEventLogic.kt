package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupTempMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KClass

object GroupTempMessageEventLogic : AbstractEventLogic<GroupTempMessageEvent>("群临时会话消息") {

    override suspend fun logic(event: GroupTempMessageEvent): Message {
        logger.info("接收到群临时会话消息,来自群:[${event.group}],会话人:[${event.senderName}],发送的消息:[${event.message.content}]")
        return MessageChainBuilder().append(executeService(event)).build();
    }

    override fun getContact(event: GroupTempMessageEvent): Contact = event.sender

    override fun getEventClass(): KClass<GroupTempMessageEvent> = GroupTempMessageEvent::class
}