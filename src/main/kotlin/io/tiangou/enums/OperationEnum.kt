package io.tiangou.enums

import io.tiangou.expection.Zhua8BotException

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
        fun getConformTypeEnum(prefix: String?) : OperationEnum  = run{
            return if (prefix == null || prefix.isBlank()) {
                TALK
            } else when (prefix) {
                TALK.prefix -> TALK
                COMMAND.prefix -> COMMAND
                UPLOAD.prefix -> UPLOAD
                DOWNLOAD.prefix -> DOWNLOAD
                else -> throw Zhua8BotException(ErrorCodeEnum.UNKNOWN_OPERATION_TYPE)
            }
        }
    }
}