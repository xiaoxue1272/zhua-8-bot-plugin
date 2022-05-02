package io.tiangou.enums

import io.tiangou.expection.Zhua8BotException
import io.tiangou.io.tiangou.enums.CommandEnum
import io.tiangou.service.*


enum class OperationEnum (
    private val operationDesc : String,
    private val prefix: String
) {

    COMMAND("指令", "/cmd"),

    TALK("聊天", "/talk"),

    UPLOAD("文件上传", "/upload"),

    DOWNLOAD("文件下载", "/download"),
    ;

    fun getConformTypeEnum(prefix : String?) : OperationService {
        return when (prefix) {
            COMMAND.prefix -> CommandService()
            TALK.prefix -> TalkService()
            UPLOAD.prefix -> UploadService()
            DOWNLOAD.prefix -> DownloadService()
            else -> {
                // 可能会为null值,所以需要后面显示声明 == true
                if (prefix?.isBlank() == true){
                    // 为空默认走聊天指令
                    TalkService()
                }
                throw Zhua8BotException(ErrorCodeEnum.UNKNOW_OPERATION_TYPE)
            }
        }
    }

}