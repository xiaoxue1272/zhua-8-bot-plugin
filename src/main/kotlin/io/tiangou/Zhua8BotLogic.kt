package io.tiangou

import io.tiangou.logic.EventLogic
import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin

const val VERSION = "0.0.3"

object Zhua8BotLogic : KotlinPlugin(
    JvmPluginDescription(
        "io.tiangou.zhua8",
        VERSION,
        "zhua8-bot-logic",
    ){
        author("Tian gou")
        info("zhua8机器人主逻辑")
    }
){

    override fun onEnable()  = with(EventLogic.eventLogicList){
        logger.info("zhua8机器人,版本号:$VERSION")
        this.forEach {
            it.loadLogic()
        }
    }

    override fun onDisable() = with(EventLogic.eventLogicList){
        logger.info("zhua8机器人,版本号:$VERSION")
        for (eventLogic in EventLogic.eventLogicList) {
            eventLogic.destroyLogic()
        }
    }


}
