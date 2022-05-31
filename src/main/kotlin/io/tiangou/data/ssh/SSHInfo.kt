package io.tiangou.data.ssh

open class SshInfo (
    open val clientFlag: String?
)
data class ShellInfo(
    override val clientFlag : String?,
    val CommandList: List<String>?,
): SshInfo(clientFlag)