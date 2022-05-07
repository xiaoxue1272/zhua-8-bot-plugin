package io.tiangou.data

import io.tiangou.constants.Constants
import net.mamoe.mirai.contact.User

class Zhua8MessageInfo(
    // 消息指令类型 可能为空 为空走聊天指令
    val prefix: String?,
    // 消息主要内容
    var body: String,
    // 消息发送人
    val sender: User
) {

}
class Zhua8MessageInfoBuilder() {

    // 消息指令前缀
    private var prefix: String? = null

    // 消息主要内容
    private var body: String? = null

    // 消息发送人
    private lateinit var sender: User

    fun prefix(prefix: String?) = apply {
        this.prefix = prefix
    }
    fun body(body: String?) = apply {
        body?.let {
            this.body = it
        }
    }
    fun sender(sender: User?)  = apply {
        sender?.let {
            this.sender = it
        }
    }

    fun build() : Zhua8MessageInfo = let{
        checkNotNull(sender){"构建Zhua8机器人消息对象时,发送者为空,请检查代码逻辑"}
        Zhua8MessageInfo(
            prefix,
            body ?: Constants.EMPTY_STRING,
            sender
        )
    }
}

