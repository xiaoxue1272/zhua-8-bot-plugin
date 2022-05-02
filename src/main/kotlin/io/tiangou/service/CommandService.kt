package io.tiangou.service

import io.tiangou.expection.NoPermissionException
import net.mamoe.mirai.contact.User
import net.mamoe.mirai.event.events.MessageEvent

class CommandService(event: MessageEvent): AbstractOperationService(event) {
    override fun init(): CommandService {
        TODO("Not yet implemented")
    }

    override fun execute(): String {
        TODO("Not yet implemented")
    }

    override fun checkUserPermission(user: User): CommandService {
        logger.info("功能没开发好,一律拦截")
        throw NoPermissionException()
    }
}