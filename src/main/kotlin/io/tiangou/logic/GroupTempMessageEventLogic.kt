package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupTempMessageEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.content
import kotlin.reflect.KClass

object GroupTempMessageEventLogic : AbstractEventLogic<GroupTempMessageEvent>("群临时会话消息") {

    override suspend fun logic(event: GroupTempMessageEvent): Message {
        log.info("接收到群临时会话消息,来自群:[{}],会话人:[{}],发送的消息:[{}]",
            event.group, event.senderName, event.message.content)
        return stringToMessage("请先添加机器人好友")
    }

    override fun getContact(event: GroupTempMessageEvent): Contact = event.sender

    override fun getEventClass(): KClass<GroupTempMessageEvent> = GroupTempMessageEvent::class
}