package io.tiangou.engine.ssh.shell

import com.jcraft.jsch.ChannelShell
import io.tiangou.constants.Constants
import io.tiangou.data.ssh.ShellInfo
import io.tiangou.engine.AbstractEngine
import io.tiangou.engine.CommonWork
import io.tiangou.engine.ssh.SSHClient
import io.tiangou.enums.SSHEnum
import io.tiangou.threadpool.CommonThreadPool
import io.tiangou.utils.Zhua8PropertiesUtils
import java.io.BufferedReader
import java.io.InputStream

object ShellEngine : AbstractEngine<CommonWork<ShellInfo, List<String>>>() {

    override fun initEngine() {
        val properties = Zhua8PropertiesUtils.loadProperties("ssh.properties")
        SSHClient.Builder()
            .setHost(properties.getProperty("default.ssh.host"))
            .setUser(properties.getProperty("default.ssh.user"))
            .setPassword(properties.getProperty("default.ssh.password"))
            .setPort(properties.getProperty("default.ssh.port").takeIf { it.isNotBlank() }?.toInt() ?: "22".toInt())
            .setClientFlag(properties.getProperty("default.ssh.clientFlag").takeIf { it.isNotBlank() } ?: Constants.DEFAULT_CLIENT_FLAG)
            .build()
    }

    override fun content(work: CommonWork<ShellInfo, List<String>>) {
        work.changeFlagWorking()
        val sshClient = SSHClient.getSSHClient(work.parameters.clientFlag ?: Constants.DEFAULT_CLIENT_FLAG)
        sshClient.execute<ChannelShell, Unit>(work.taskNo, SSHEnum.SHELL, work.isLifeCycleFinish()) {
                outputStream.apply {
                    work.parameters.CommandList.forEach {
                        this.write(it.toByteArray(Charsets.UTF_8))
                        this.flush()
                    }
                }
                if (work.isLifeCycleFinish()) {
                    outputStream.close()
                    sshClient.reset()
                }
            }
    }

    private fun startReaderTerminal(inputStream: InputStream, terminalLines: MutableList<String>) {
        CommonThreadPool.execute {
            val reader = BufferedReader(inputStream.reader(Charsets.UTF_8))
            while (true) {
                val readLine = reader.readLine() ?: break
                println(readLine)
                terminalLines.add(readLine)
            }
        }
    }
}