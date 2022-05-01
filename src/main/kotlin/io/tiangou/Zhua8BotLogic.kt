package io.tiangou

import io.tiangou.logic.EventLogic
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

const val VERSION = "0.0.1";

object Zhua8BotLogic : KotlinPlugin(
    JvmPluginDescription(
        id = "io.tiangou.zhua8",
        name = "zhua8-bot-logic",
        version = VERSION,
    )
    {
        author("Tian gou")
    }
)
{
    override fun onEnable() {
        logger.info { "zhua8机器人,版本号:$VERSION" }
        for (eventLogic in EventLogic.eventLogicList) {
            eventLogic.loadLogic()
        }
    }

    override fun onDisable() {
        logger.info { "zhua8机器人,版本号:$VERSION" }
        for (eventLogic in EventLogic.eventLogicList) {
            eventLogic.loadLogic()
        }
    }

}
