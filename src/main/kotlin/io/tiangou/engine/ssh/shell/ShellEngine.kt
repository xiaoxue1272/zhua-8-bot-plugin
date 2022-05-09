package io.tiangou.engine.ssh.shell

import com.jcraft.jsch.ChannelShell
import io.tiangou.constants.Constants
import io.tiangou.data.ssh.ShellInfo
import io.tiangou.engine.AbstractEngine
import io.tiangou.engine.CommonWork
import io.tiangou.engine.ssh.SSHClient
import io.tiangou.enums.SSHEnum
import io.tiangou.utils.Zhua8PropertiesUtils
import java.io.InputStream

object ShellEngine : AbstractEngine<CommonWork<ShellInfo, List<String>>>() {

    override fun initEngine() {
        val properties = Zhua8PropertiesUtils.loadProperties("ssh.properties")
        var clientFlags = properties.getProperty("ssh.clientFlag")
        if (clientFlags == null || clientFlags.isBlank()) {
            clientFlags = Constants.DEFAULT_CLIENT_FLAG
        }
        clientFlags.split(Constants.COMMA).forEach {
            SSHClient.Builder()
                .setHost(properties.getProperty("default.ssh.host"))
                .setUser(properties.getProperty("default.ssh.user"))
                .setPassword(properties.getProperty("default.ssh.password"))
                .setPort(properties.getProperty("default.ssh.port").takeIf { it.isNotBlank() }?.toInt() ?: "22".toInt())
                .setClientFlag(it)
                .build()
        }
    }

    override fun content(work: CommonWork<ShellInfo, List<String>>) {
        work.changeFlagWorking()
        val sshClient = SSHClient.getSSHClient(work.parameters.clientFlag ?: Constants.DEFAULT_CLIENT_FLAG)
        work.result = sshClient.execute<ChannelShell, List<String>>(work.taskNo, SSHEnum.SHELL, work.isLifeCycleFinish()) {
            val inputStream = inputStream
            readTerminal(inputStream, sshClient)
            val commandList = work.parameters.CommandList
            val outputStream = outputStream
            if (commandList != null) {
                for (command in commandList) {
                    outputStream.write(command.toByteArray(Charsets.UTF_8))
                    outputStream.flush()
                }
            }
            readTerminal(inputStream, sshClient)
            if (work.isLifeCycleFinish()) {
                outputStream.close()
            }
            listOf(sshClient.concurrentTerminalString ?: "未读取到终端返回消息")
        }
    }

    private fun readTerminal(inputStream: InputStream, sshClient: SSHClient) {
        var tires = 0
        sshClient.currentTerminalRead = false
        sshClient.concurrentTerminalString = null
        while (tires < 3) {
            val availableBytes = inputStream.available()
            if (availableBytes < 1) {
                Thread.sleep(1000)
                tires ++
                continue
            }
            val readBytes = ByteArray(availableBytes)
            inputStream.read(readBytes)
            sshClient.concurrentTerminalString = String(readBytes, Charsets.UTF_8)
            break
        }
        sshClient.currentTerminalRead = true
    }
}