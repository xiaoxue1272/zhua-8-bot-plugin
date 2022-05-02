package io.tiangou.service

import io.tiangou.constants.Constants
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.content
import net.mamoe.mirai.message.data.firstIsInstanceOrNull
import net.mamoe.mirai.message.data.source

class TalkService(event: MessageEvent): AbstractOperationService(event) {
    override fun init(): TalkService {
        logger.info("测试demo,先不写那么多,累了,进来消息一律复读")
        return this
    }

    override fun execute(): String {
        return event.message.content.replace("@${event.bot.id}", "")
    }
}