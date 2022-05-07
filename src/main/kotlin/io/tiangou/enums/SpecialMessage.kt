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
        "搞点黄色",
        "-dtrue -cdefault -easync 'screen -r zombie' 'save'",
        "ojbk"
    ),;

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
