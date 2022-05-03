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
        fun getConformTypeEnum(prefix: String?) : OperationEnum {
            if (prefix == null || prefix.isBlank()) {
                return TALK
            }
            return when (prefix) {
                TALK.prefix -> TALK
                COMMAND.prefix -> COMMAND
                UPLOAD.prefix -> UPLOAD
                DOWNLOAD.prefix -> DOWNLOAD
                else -> throw Zhua8BotException(ErrorCodeEnum.UNKNOWN_OPERATION_TYPE)
            }
        }
    }
}