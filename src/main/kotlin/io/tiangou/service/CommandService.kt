package io.tiangou.service

import io.tiangou.data.Zhua8MessageInfo
import io.tiangou.expection.NoPermissionException

class CommandService(event: Zhua8MessageInfo): AbstractOperationService(event) {
    override fun init(): CommandService {
        TODO("Not yet implemented")
    }

    override fun execute(): String {
        TODO("Not yet implemented")
    }

    override fun checkUserPermission(messageInfo: Zhua8MessageInfo): CommandService {
        logger.info("功能没开发好,一律拦截")
        throw NoPermissionException()
    }
}