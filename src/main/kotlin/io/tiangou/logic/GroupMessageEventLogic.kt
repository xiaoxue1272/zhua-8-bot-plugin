package io.tiangou.logic

import io.tiangou.constants.Constants
import io.tiangou.enums.SpecialMessageEnum
import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.*
import net.mamoe.mirai.message.data.MessageSource.Key.quote
import kotlin.reflect.KClass

object GroupMessageEventLogic : AbstractEventLogic<GroupMessageEvent>("群内at") {

    override suspend fun logic(event: GroupMessageEvent) : Message? {
        return if (event.message.firstIsInstanceOrNull<At>()?.target == event.bot.id) {
             onAt(event)
        } else {
            null
        }
    }

    private fun onAt(event: GroupMessageEvent) : Message {
        // 如果消息中能够获取到At对象实例,且At目标为Bot
        log.info("接受到at,消息内容:[{}],群名称:[{}],at人群名片:[{}]",
            event.message.content, event.group.name, event.senderName)
        return stringToMessage(executeService(event))
    }

    override fun <E : MessageEvent> executeService(event: E): List<String> {
        // 因群内消息只监听at,所以要特殊处理一下,把at的那部分去掉
        val message = event.message.content.replace("@${event.bot.id}", Constants.EMPTY_STRING).trimStart()
        val zhua8MessageInfo = event.run {
            SpecialMessageEnum.getMessageInfoIfSpecial(message, sender)
                ?: convertMessage(message, sender)
        }
        val operation = judgeMessageOperation(zhua8MessageInfo.prefix)
        log.info("当前监听到的群内at消息解析后的操作类型最终判断为:[{}],开始执行对应下游服务逻辑", operation.desc)
        doService(zhua8MessageInfo, operation).let {
            return zhua8MessageInfo.specifyReplyMessages ?: it
        }
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
