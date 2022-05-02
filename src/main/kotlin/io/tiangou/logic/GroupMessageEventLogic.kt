package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*
import kotlin.reflect.KClass

class GroupMessageEventLogic : AbstractEventLogic<GroupMessageEvent>("群内at") {

    override suspend fun logic(event: GroupMessageEvent) : Message? {
        if (event.message.firstIsInstanceOrNull<At>()?.target == event.bot.id) {
            return onAt(event);
        }
        return null;
    }

    private fun onAt(event: GroupMessageEvent) : Message {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        logger.info("接受到at,消息内容:[${event.message.content}],群名称:${event.group.name},at人群名片:[${event.senderName}]")
        return QuoteReply(event.message.source).plus("窝嫩爹");
    }

    override fun getEventClass(): KClass<GroupMessageEvent> {
        return GroupMessageEvent::class
    }

    override fun getContact(event: GroupMessageEvent): Contact {
        return event.group;
    }
}