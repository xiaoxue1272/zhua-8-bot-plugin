package io.tiangou.service

import io.tiangou.data.Zhua8MessageInfo

class UploadService(messageInfo: Zhua8MessageInfo): AbstractOperationService(messageInfo) {
    override fun init(): UploadService {
        // todo 后面开发
        return this
    }

    override fun execute(): List<String> {
        // todo 后面开发
        return arrayListOf("功能懒得写,后面再说")
    }
}