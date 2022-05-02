package io.tiangou.service.factory

import io.tiangou.enums.OperationEnum
import io.tiangou.service.*
import net.mamoe.mirai.event.Event
import net.mamoe.mirai.event.events.MessageEvent
import net.mamoe.mirai.message.data.Message

object DefaultOperationFactory : OperationServiceFactory {

    override fun <E: MessageEvent> createOperationService(event: E, operationEnum: OperationEnum): OperationService {
        return when (operationEnum) {
            OperationEnum.COMMAND -> CommandService(event)
            OperationEnum.TALK -> TalkService(event)
            OperationEnum.UPLOAD -> UploadService(event)
            OperationEnum.DOWNLOAD -> DownloadService(event)
        }
    }


}