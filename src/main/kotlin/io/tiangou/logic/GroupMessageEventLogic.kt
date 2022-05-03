package io.tiangou.logic

import io.tiangou.constants.EMPTY_STRING
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import kotlin.reflect.KClass

object GroupMessageEventLogic : AbstractEventLogic<GroupMessageEvent>("群内at") {

    override suspend fun logic(event: GroupMessageEvent) : Message? {
        return if (event.message.firstIsInstanceOrNull<At>()?.target == event.bot.id) {
             onAt(event);
        } else {
            null
        }
    }

    private fun onAt(event: GroupMessageEvent) : Message {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        logger.info("接受到at,消息内容:[${event.message.content}],群名称:${event.group.name},at人群名片:[${event.senderName}]")
        return stringToMessage(executeService(event));
    }

    override fun <E : MessageEvent> executeService(event: E): String {
        // 因群内消息只监听at,所以要特殊处理一下,把at的那部分去掉
        val mainMessage = event.message.content.replace("@${event.bot.id}", EMPTY_STRING).trimStart();
        val messageInfo = convertMessage(mainMessage, event.sender);
        val operation = judgeMessageOperation(messageInfo);
        logger.info("当前监听到的群内at消息解析后的操作类型最终判断为:[${operation.desc}],开始执行对应下游服务逻辑")
        return doService(messageInfo, operation)
    }

    override fun getEventClass(): KClass<GroupMessageEvent> = GroupMessageEvent::class

    override fun getContact(event: GroupMessageEvent): Contact  = event.group

    override fun addMessageHeader(message: Message, event: GroupMessageEvent): Message {
        return MessageChainBuilder().append(event.message.quote()).apply {
            if (event.sender.nameCard.isNotEmpty()) {
                this.append(event.sender.at())
            }
        }.append(message).build()
    }
}
