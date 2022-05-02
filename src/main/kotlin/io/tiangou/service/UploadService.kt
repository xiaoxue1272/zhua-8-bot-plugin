package io.tiangou.service

import net.mamoe.mirai.event.events.MessageEvent

class UploadService(event: MessageEvent): AbstractOperationService(event) {
    override fun init(): UploadService {
        // todo 后面开发
        return this
    }

    override fun execute(): String {
        // todo 后面开发
        return "功能懒得写,后面再说"
    }
}