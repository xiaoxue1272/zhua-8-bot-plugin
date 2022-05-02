package io.tiangou.io.tiangou.enums

enum class CommandEnum(val command : String, val desc: String) {

    SHELL("shell", "shell脚本"),

    CRON_SHELL("cron_shell", "cron定时任务脚本"),

}