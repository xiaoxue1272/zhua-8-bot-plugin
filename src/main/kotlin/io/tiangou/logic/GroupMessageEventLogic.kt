package io.tiangou.logic

import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import kotlin.reflect.KClass

class GroupMessageEventLogic : AbstractEventLogic<GroupMessageEvent>("at") {

    override suspend fun logic(event: GroupMessageEvent) {
        onAt(event)
    }

    private suspend fun onAt(event: GroupMessageEvent) {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        if (event.message.firstIsInstanceOrNull<At>()?.target == event.bot.id) {
            logger.info("接受到at,消息内容:[${event.message.content}],群名称:${event.group.name},at人群名片:[${event.senderName}]")
            val replyMessageChain = At(event.sender.id).plus("窝嫩爹");
            if (event.group.sendMessage(replyMessageChain).isToGroup) {
                logger.info("成功回复at人消息:${replyMessageChain.content},群名称:${event.group.name},at人群名片:[${event.senderName}]")
            }
        }
    }

    override fun getEventClass(): KClass<GroupMessageEvent> {
        return GroupMessageEvent::class
    }
}