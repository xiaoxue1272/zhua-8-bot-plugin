package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.StrangerMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KClass

class StrangerMessageEventLogic: AbstractEventLogic<StrangerMessageEvent>("陌生人消息") {

    override suspend fun logic(event: StrangerMessageEvent): Message {
        logger.info("接收到陌生人:[${event.stranger.nick}]发送的消息:[${event.message.content}]")
        return MessageChainBuilder().build().plus("请先添加机器人好友");
    }

    override fun getEventClass(): KClass<StrangerMessageEvent> {
        return StrangerMessageEvent::class
    }

    override fun getContact(event: StrangerMessageEvent): Contact {
        return event.stranger;
    }
}