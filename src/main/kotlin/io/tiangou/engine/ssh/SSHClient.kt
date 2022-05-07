package io.tiangou.engine.ssh

import com.jcraft.jsch.Channel
import com.jcraft.jsch.JSch
import com.jcraft.jsch.JSchException
import com.jcraft.jsch.Session
import io.tiangou.enums.ErrorCodeEnum
import io.tiangou.enums.SSHEnum
import io.tiangou.expection.Zhua8BotException
import net.mamoe.mirai.console.util.safeCast
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference


class SSHClient constructor(
    host: String,
    port: Int,
    user: String,
    password: String,
) {

    /**
     * 会话对象
     */
    val session: Session

    /**
     * 当前管道对象
     * 简单来说就是,会话通讯对象
     */
    var channel: Channel? = null

    /**
     * 当前ssh客户端使用者标识
     */
    val currentFlagReference : AtomicReference<String> = AtomicReference(READY)

    init {
        try {
            session = JSCH_INSTANCE.getSession(user, host, port)!!.apply {
                setPassword(password)
                setConfig("StrictHostKeyChecking", "no")
                connect()
            }
        } catch (e: JSchException) {
            throw Zhua8BotException(ErrorCodeEnum.CREATE_SSH_CLIENT_ERROR)
        }
    }

    fun reset() {
        currentFlagReference.set(READY)
    }

    /**
     * 执行
     */
    inline fun <reified C: Channel, R> execute(userFlag: String, sshEnum: SSHEnum, isDone: Boolean, exec: C.() -> R) : R {
        var result : R? = null
        currentFlagReference.get()?.apply {
            takeIf {
                it == userFlag || it == READY
            }?.apply {
                currentFlagReference.compareAndSet(this, userFlag)
                channel = channel.safeCast() ?: sshEnum.createChannel(session).safeCast()
                channel?.apply {
                    if (!session.isConnected) {
                        session.connect()
                    }
                    takeIf{
                        !it.isConnected && SSHEnum.ConnectType.FIRST == sshEnum.connetcType
                    }?.connect()
                    channel.safeCast<C>()?.let {
                        result = exec(it)
                    }
                    takeIf{
                        !it.isConnected && SSHEnum.ConnectType.LAST == sshEnum.connetcType
                    }?.connect()
                    takeIf { isDone }?.apply {
                        disconnect()
                        reset()
                    }
                }
            }
        }
        return result ?: throw Zhua8BotException(ErrorCodeEnum.SSH_CLIENT_IS_USING)
    }


    class Builder {
        private var user: String? = null
        private var host: String? = null
        private var password: String? = null
        private var port: Int? = null
        private var clientFlag: String? = null

        fun setUser(user: String): Builder = apply { this.user = user }

        fun setHost(host: String): Builder = apply { this.host = host }

        fun setPassword(password: String): Builder = apply { this.password = password }

        fun setClientFlag(clientFlag: String): Builder = apply { this.clientFlag = clientFlag }

        fun setPort(port: Int): Builder = apply { this.port = port }


        fun build(): SSHClient {
            if (Objects.isNull(host) || host!!.isBlank()) {
                throw Zhua8BotException(ErrorCodeEnum.SSH_CLIENT_HOST_NULL)
            }
            if (Objects.isNull(password) || password!!.isBlank()) {
                throw Zhua8BotException(ErrorCodeEnum.SSH_CLIENT_PASSWORD_NULL)
            }
            if (Objects.isNull(clientFlag) || clientFlag!!.isBlank()) {
                throw Zhua8BotException(ErrorCodeEnum.SSH_CLIENT_FLAG_NULL)
            }
            if (Objects.isNull(user) || user!!.isBlank()) {
                user = "root"
            }
            return SSHClient(host!!, port ?: 22, user!!, password!!).apply {
                SSH_CLIENT_MAP.put(clientFlag!!, this)
            }
        }
    }

    companion object {
        @JvmStatic
        private val JSCH_INSTANCE = JSch()

        const val READY = "READY"

        /**
         * clientId(host+user) key 客户端实例 value
         */
        @JvmStatic
        private val SSH_CLIENT_MAP = ConcurrentHashMap<String, SSHClient>()

        fun getSSHClient(clientFlag: String) : SSHClient {
            return SSH_CLIENT_MAP.get(clientFlag) ?: throw Zhua8BotException(ErrorCodeEnum.SSH_CLIENT_NOT_EXISTS)
        }

    }

}