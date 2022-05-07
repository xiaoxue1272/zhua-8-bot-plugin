package io.tiangou.data.ssh

open class SSHInfo (
    open val clientFlag: String?
)
data class ShellInfo(
    override val clientFlag : String?,
    val CommandList: List<String>
): SSHInfo(clientFlag)