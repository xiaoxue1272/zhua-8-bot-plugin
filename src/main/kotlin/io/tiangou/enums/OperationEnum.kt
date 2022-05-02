package io.tiangou.enums

import io.tiangou.expection.Zhua8BotException
import io.tiangou.io.tiangou.enums.CommandEnum
import io.tiangou.service.*


enum class OperationEnum (
    val desc : String,
    val prefix: String
) {

    COMMAND("指令", "/cmd"),

    TALK("聊天", "/talk"),

    UPLOAD("文件上传", "/upload"),

    DOWNLOAD("文件下载", "/download"),
    ;

    companion object {
        val operationRegex: Regex =  Regex("/\\w+");
        fun getConformTypeEnum(message : String) : OperationEnum {
            val findResult = operationRegex.find(message)?.value;
            if (message.isBlank() || findResult == null || !message.startsWith(findResult)) {
                return TALK
            } else if (message.startsWith(COMMAND.prefix)) {
                return COMMAND
            } else if (message.startsWith(TALK.prefix)) {
                return TALK
            } else if (message.startsWith(UPLOAD.prefix)) {
                return UPLOAD
            } else if (message.startsWith(DOWNLOAD.prefix)) {
                return DOWNLOAD
            } else {
                throw Zhua8BotException(ErrorCodeEnum.UNKNOWN_OPERATION_TYPE)
            }
        }
    }
}