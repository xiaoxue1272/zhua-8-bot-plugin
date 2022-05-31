package io.tiangou.enums

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelExec
import com.jcraft.jsch.ChannelShell
import io.tiangou.expection.Zhua8BotException


enum class SshEnum(val code: String, val initChannel : Channel.() -> Channel, val connetcType: ConnectType){

    SFTP("sftp", {
        (this as? ChannelSftp)?.apply {

        } ?: throw Zhua8BotException(ErrorCodeEnum.CREATE_SSH_CLIENT_CHANNEL_FAILED)
        this
    }, ConnectType.FIRST),

//    EXEC("exec", {
//        (this as? ChannelExec)?.apply {
//            setPty(true)
//        } ?: throw Zhua8BotException(ErrorCodeEnum.CREATE_SSH_CLIENT_CHANNEL_FAILED)
//        this
//    }, ConnectType.LAST),

    SHELL("shell", {
        (this as? ChannelShell)?.apply {
            setPty(true)
        } ?: throw Zhua8BotException(ErrorCodeEnum.CREATE_SSH_CLIENT_CHANNEL_FAILED)
        this
    }, ConnectType.FIRST),
    ;

    enum class ConnectType {
        FIRST,
        LAST,
    }

}

