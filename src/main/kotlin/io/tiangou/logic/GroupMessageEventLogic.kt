package io.tiangou.logic

import io.tiangou.enums.OperationEnum
import net.mamoe.mirai.console.permission.PermissionService
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import kotlin.reflect.KClass

object GroupMessageEventLogic : AbstractEventLogic<GroupMessageEvent>("群内at") {

    override suspend fun logic(event: GroupMessageEvent) : Message? {
        if (event.message.firstIsInstanceOrNull<At>()?.target == event.bot.id) {
            return onAt(event);
        }
        return null;
    }

    private fun onAt(event: GroupMessageEvent) : Message {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        logger.info("接受到at,消息内容:[${event.message.content}],群名称:${event.group.name},at人群名片:[${event.senderName}]")
        return getReplyMessage(event, executeService(event));
    }


    private fun getReplyMessage(event: GroupMessageEvent, message: String): MessageChain {
        val replyMessageChainBuilder = MessageChainBuilder().append(event.message.quote());
        if (event.sender.nameCard.isNotEmpty()) {
            replyMessageChainBuilder.append(event.sender.at())
        }
        return replyMessageChainBuilder.append(message).build()
    }

    override fun <E : MessageEvent> executeService(event: E): String {
        // 因群内消息只监听at,所以要特殊处理一下,把at的那部分去掉
        val enum = OperationEnum.getConformTypeEnum(event.message.content.replace("@${event.bot.id}", "").trimStart())
        logger.info("当前监听到的群内at消息解析后的操作类型最终判断为:[${enum.desc}],开始执行对应下游服务逻辑")
        return doService(event, enum)
    }

    override fun getEventClass(): KClass<GroupMessageEvent> = GroupMessageEvent::class

    override fun getContact(event: GroupMessageEvent): Contact  = event.group

}
