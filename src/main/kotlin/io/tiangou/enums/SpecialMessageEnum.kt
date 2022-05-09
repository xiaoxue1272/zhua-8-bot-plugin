package io.tiangou.enums

import io.tiangou.data.Zhua8MessageInfo

enum class SpecialMessageEnum(
    val operationEnum : OperationEnum,
    val message : String,
    val actualReplacementMessage : String,
    val reply : String
){
    SAVE_ZOMBIE_PROJECT_SERVER(
        OperationEnum.COMMAND,
        "保存服务器",
        "-dtrue -cdefault -easync [screen -d zombie] [screen -r zombie] [save]",
        "保存中"
    ),

    STOP_ZOMBIE_PROJECT_SERVER(
        OperationEnum.COMMAND,
        "停止服务器",
        "-dtrue -cdefault -easync [screen -d zombie] [screen -r zombie] [quit]",
        "开始停止Project Zombiod Server"
    ),

    START_ZOMBIE_PROJECT_SERVER(
        OperationEnum.COMMAND,
        "启动服务器",
        "-dtrue -cdefault -easync [cd /home/steam/projectZomboidServer] [./start-server.sh]",
        "开始启动Project Zombiod Server"
    ),
    ;

   companion object {
       fun getEnumIfMessageSpecial(message: Zhua8MessageInfo) : SpecialMessageEnum? {
           for (enum in values()) {
               if (enum.message == message.body && enum.operationEnum.prefix == message.prefix) {
                   return enum
               }
           }
           return null
       }
   }
}
