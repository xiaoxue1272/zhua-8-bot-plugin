package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.StrangerMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KClass

object StrangerMessageEventLogic: AbstractEventLogic<StrangerMessageEvent>("陌生人消息") {

    override suspend fun logic(event: StrangerMessageEvent): Message {
        logger.info("接收到陌生人:[${event.stranger.nick}]发送的消息:[${event.message.content}]")
        return MessageChainBuilder().append("请先添加机器人好友").build();
    }

    override fun getEventClass(): KClass<StrangerMessageEvent> = StrangerMessageEvent::class

    override fun getContact(event: StrangerMessageEvent): Contact = event.stranger
}