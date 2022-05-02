package io.tiangou.logic

import net.mamoe.mirai.contact.Contact
import net.mamoe.mirai.event.events.NewFriendRequestEvent
import net.mamoe.mirai.message.data.Message
import net.mamoe.mirai.message.data.MessageChainBuilder
import kotlin.reflect.KClass

class NewFriendRequestEventLogic : AbstractEventLogic<NewFriendRequestEvent>("好友申请") {

    override suspend fun logic(event: NewFriendRequestEvent) : Message {
        logger.info("Bot接受到好友申请,申请人昵称:[${event.fromNick}],QQ号:[${event.fromId}]")
        event.accept()
        return MessageChainBuilder().build().plus("已同意好友申请")
    }

    override fun getEventClass(): KClass<NewFriendRequestEvent> {
        return NewFriendRequestEvent::class
    }

    override fun getContact(event: NewFriendRequestEvent): Contact? {
        return event.bot.getFriend(event.fromId);
    }
}