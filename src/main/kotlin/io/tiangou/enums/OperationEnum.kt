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

//    HELP("帮助", "/help"),
    ;

    companion object {
        fun getConformTypeEnum(prefix: String?) : OperationEnum  = run{
            if (prefix == null || prefix.isBlank()) {
                return@run TALK
            } else {
                for (enum in values()) {
                    if (prefix == enum.prefix) {
                        return@run enum
                    }
                }
                throw Zhua8BotException(ErrorCodeEnum.UNKNOWN_OPERATION_TYPE)
            }
        }
    }
}