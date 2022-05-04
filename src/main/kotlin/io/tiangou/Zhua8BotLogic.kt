package io.tiangou

import io.tiangou.logic.EventLogic
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.utils.info

const val VERSION = "0.0.2";

object Zhua8BotLogic : KotlinPlugin(
    JvmPluginDescription(
        id = "io.tiangou.zhua8",
        name = "zhua8-bot-logic",
        version = VERSION,
    )
    {
        author("Tian gou")
        info("zhua8机器人主逻辑")
    }
)
{
    override fun onEnable()  = with(EventLogic.eventLogicList){
        logger.info { "zhua8机器人,版本号:$VERSION" }
        this.forEach {
            it.loadLogic()
        }
    }

    override fun onDisable() = with(EventLogic.eventLogicList){
        logger.info { "zhua8机器人,版本号:$VERSION" }
        for (eventLogic in EventLogic.eventLogicList) {
            eventLogic.loadLogic()
        }
    }

}
