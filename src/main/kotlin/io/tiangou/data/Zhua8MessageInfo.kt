package io.tiangou.data

import net.mamoe.mirai.contact.User

class Zhua8MessageInfo(
    // 消息指令类型 可能为空 为空走聊天指令
    val prefix: String?,
    // 消息主要内容
    val body: String,
    // 消息发送人
    val sender: User
)