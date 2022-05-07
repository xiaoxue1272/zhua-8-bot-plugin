package io.tiangou.service

import io.tiangou.data.Zhua8MessageInfo

class TalkService(messageInfo: Zhua8MessageInfo): AbstractOperationService(messageInfo) {
    override fun init(): TalkService {
        logger.info("测试demo,先不写那么多,累了,进来消息一律复读")
        return this
    }

    override fun execute(): List<String> {
        return arrayListOf(messageInfo.body)
    }
}