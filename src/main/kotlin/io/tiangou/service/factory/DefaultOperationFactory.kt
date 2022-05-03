package io.tiangou.service.factory

import io.tiangou.data.Zhua8MessageInfo
import io.tiangou.enums.OperationEnum
import io.tiangou.service.*
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Message

object DefaultOperationFactory : OperationServiceFactory {

    override fun createOperationService(messageInfo: Zhua8MessageInfo, operation: OperationEnum): OperationService {
        return when (operation) {
            OperationEnum.COMMAND -> CommandService(messageInfo)
            OperationEnum.TALK -> TalkService(messageInfo)
            OperationEnum.UPLOAD -> UploadService(messageInfo)
            OperationEnum.DOWNLOAD -> DownloadService(messageInfo)
        }
    }


}