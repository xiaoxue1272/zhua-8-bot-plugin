package io.tiangou.engine

import com.jcraft.jsch.*
import io.tiangou.Zhua8BotLogic
import io.tiangou.constants.Constants
import io.tiangou.data.ssh.ShellInfo
import io.tiangou.data.ssh.SshInfo
import io.tiangou.enums.ErrorCodeEnum
import io.tiangou.enums.SshEnum
import io.tiangou.expection.Zhua8BotException
import io.tiangou.utils.Zhua8PropertiesUtils
import kotlinx.coroutines.launch
import java.io.InputStream
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicReference

interface Engine<T : Work<*>> {

    fun execute(work: T)

}
sealed class AbstractEngine<T : Work<*>> : Engine<T> {

    private var isInit = false

    abstract fun init()

    override fun execute(work: T) {
        if (!isInit) {
            init()
            isInit = true
        }
        when (work.workType()) {
            Work.WorkType.SYNC -> {
                content(work)
            }
            Work.WorkType.ASYNC -> Zhua8BotLogic.launch{
                content(work)
            }
        }
    }

    abstract fun content(work: T)

}
sealed class SshEngine<T : SshInfo, R> : AbstractEngine<CommonWork<T, R>>() {

    override fun init() {
        val propertyMap = Zhua8PropertiesUtils.loadProperties("ssh.properties")
        var clientFlags = propertyMap.get("ssh.clientFlag")
        if (clientFlags == null || clientFlags.isBlank()) {
            clientFlags = Constants.DEFAULT_CLIENT_FLAG
        }
        clientFlags.split(Constants.COMMA).forEach {
            val sshClientBuilder = SshClient.Builder()
            sshClientBuilder.setClientFlag(it)
            propertyMap.get("$it.ssh.host")?.also {
                sshClientBuilder.setHost(it)
            }
            propertyMap.get("$it.ssh.user")?.also {
                sshClientBuilder.setUser(it)
            }
            propertyMap.get("$it.ssh.password")?.also {
                sshClientBuilder.setPassword(it)
            }
            propertyMap.get("$it.ssh.user")?.also {
                sshClientBuilder.setUser(it)
            }
            propertyMap.get("$it.ssh.port")?.also {
                it.takeIf { it.isNotBlank() }?.toInt() ?: "22".toInt()
            }
            sshClientBuilder.build()
        }
    }

    internal abstract fun operateClient(client: SshClient, work: CommonWork<T, R>)

    override fun content(work: CommonWork<T, R>) {
        work.changeFlagWorking()
        val client = SshClient.getSSHClient(work.parameters.clientFlag ?: Constants.DEFAULT_CLIENT_FLAG)
        operateClient(client, work)
    }

}
internal class SshClient constructor(
    val host: String,
    val port: Int,
    val user: String,
    val password: String,
) {

    /**
     * 会话对象
     */
    var session: Session? = null

    /**
     * 当前管道对象
     * 简单来说就是,会话通讯对象
     */
    var channel: Channel? = null

    /**
     * 当前ssh客户端使用者标识
     */
    val currentFlagReference : AtomicReference<String> = AtomicReference(READY)

    var isReady: Boolean = false


    private fun connectToSession(): Session {
        try {
            return JSCH_INSTANCE.getSession(user, host, port)!!.apply {
                setPassword(password)
                setConfig("StrictHostKeyChecking", "no")
                connect()
            }
        } catch (e: JSchException) {
            throw Zhua8BotException(ErrorCodeEnum.CREATE_SSH_CLIENT_ERROR)
        }
    }

    private fun createChannel(session: Session, enum: SshEnum): Channel {
        return session.run {
            openChannel(enum.code).apply {
                enum.initChannel(this)
            }
        }
    }

    fun init(sshEnum: SshEnum): Boolean {
        if (!isReady) {
            if (session == null || !session!!.isConnected) {
                session = connectToSession()
            }
            if (channel == null || !channel!!.isConnected) {
                channel = createChannel(session!!, sshEnum)
            }
            isReady = true
        }
        return true
    }

    fun isFirstConnect(): Boolean = READY == currentFlagReference.get()

    fun destroy() {
        if (isReady) {
            channel!!.disconnect()
            session!!.disconnect()
            resetFlag()
        }
    }

    private fun resetFlag() {
        currentFlagReference.set(READY)
    }

    fun checkCurrentClientIsUsingAndSetUsedFlag(currentFlag: String,userFlag: String): Boolean =
        currentFlagReference.compareAndSet(currentFlag, userFlag)

    /**
     * 执行
     */
    inline fun <reified C: Channel, R> execute(userFlag: String, enum: SshEnum, isDone: Boolean, exec: C.() -> R) : R {
        var result : R? = null
        currentFlagReference.get()?.apply {
            takeIf {
                it == userFlag || it == READY
            }?.apply {
                if (checkCurrentClientIsUsingAndSetUsedFlag(this, userFlag)) {
                    init(enum)
                    if (!channel!!.isConnected && SshEnum.ConnectType.FIRST == enum.connetcType) {
                        channel!!.connect()
                    }
                    (channel!! as? C)?.let {
                        result = exec(it)
                    }
                    if (!channel!!.isConnected && SshEnum.ConnectType.LAST == enum.connetcType) {
                        channel!!.connect()
                    }
                    if (isDone) {
                        destroy()
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


        fun build(): SshClient {
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
            return SshClient(host!!, port ?: 22, user!!, password!!).apply {
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
        private val SSH_CLIENT_MAP = ConcurrentHashMap<String, SshClient>()

        fun getSSHClient(clientFlag: String) : SshClient {
            return SSH_CLIENT_MAP.get(clientFlag) ?: throw Zhua8BotException(ErrorCodeEnum.SSH_CLIENT_NOT_EXISTS)
        }

    }

}
object ShellEngine: SshEngine<ShellInfo, List<String>>() {

    override fun operateClient(client: SshClient, work: CommonWork<ShellInfo, List<String>> ) {
        client.execute<ChannelShell, List<String>>(work.taskNo, SshEnum.SHELL, work.isLifeCycleFinish()) {
            val outputStream = outputStream
            val inputStream = inputStream
            takeIf { client.isFirstConnect() }?.run { loadTerminal(inputStream) }
            val terminalLines = work.parameters.CommandList?.forEach {
                outputStream.write(it.toByteArray(Charsets.UTF_8))
                outputStream.flush()
            }.run { readTerminal(inputStream) }
            takeIf { work.isLifeCycleFinish() }.run{
                outputStream.close()
                inputStream.close()
            }
            terminalLines?.takeIf { it.isNotEmpty() } ?: listOf( "未读取到终端返回消息")
        }
    }


    private fun readTerminal(inputStream: InputStream): List<String>? =
        loadTerminal(inputStream)?.run {
            String(this.filter{ it > 0 }.toByteArray(), Charsets.UTF_8)
                .split(Constants.NEW_LINE_DELIMITER, Constants.ENTER_DELIMITER)
        }

    private fun loadTerminal(inputStream: InputStream) : ByteArray? {
        var tires = 0
        while (tires < 4) {
            val availableBytes = inputStream.available()
            if (availableBytes < 1) {
                Thread.sleep(500)
                tires ++
                continue
            }
            return ByteArray(availableBytes).apply {
                inputStream.read(this)
            }
        }
        return null
    }

}