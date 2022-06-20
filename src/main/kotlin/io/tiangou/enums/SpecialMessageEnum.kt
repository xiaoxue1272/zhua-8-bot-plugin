package io.tiangou.enums

import io.tiangou.data.Zhua8MessageInfo
import net.mamoe.mirai.contact.User

enum class SpecialMessageEnum(
    val operationEnum : OperationEnum,
    val message : String,
    val actualReplacementMessage : String,
    val replyMessages : List<String>
){
    SAVE_ZOMBIE_PROJECT_SERVER(
        OperationEnum.COMMAND,
        "保存僵毁服务器",
        "-dtrue -cdefault -easync [screen -d zombie] [screen -r zombie] [save]",
        listOf("保存中")
    ),

    STOP_ZOMBIE_PROJECT_SERVER(
        OperationEnum.COMMAND,
        "停止僵毁服务器",
        "-dtrue -cdefault -easync [screen -d zombie] [screen -r zombie] [quit]",
        listOf("开始停止Project Zombiod Server")
    ),

    START_ZOMBIE_PROJECT_SERVER(
        OperationEnum.COMMAND,
        "启动僵毁服务器",
        "-dtrue -cdefault -easync [screen -d zombie] [screen -r zombie] [cd /home/steam/projectZomboidServer] [./start-server.sh]",
        listOf("开始启动Project Zombiod Server")
    ),

    START_LIVEGO_SERVER(
        OperationEnum.COMMAND,
        "强制启动直播服务器",
        "-dtrue -clivego -esync [screen -X -S livego quit] [screen -S livego] [cd /home/livego/livego] [./livego]",
        listOf("开始启动直播服务器")
    ),
    ;

   companion object {
       fun getMessageInfoIfSpecial(message: String, sender: User) : Zhua8MessageInfo? {
           for (enum in values()) {
               val prefix = enum.operationEnum.prefix
               if (message.contains(prefix) && message.contains(enum.message)) {
                   return Zhua8MessageInfo.Builder()
                       .prefix(prefix)
                       .body(enum.actualReplacementMessage)
                       .sender(sender)
                       .specifyReplyMessages(enum.replyMessages)
                       .build()
               }
           }
           return null
       }
   }
}
