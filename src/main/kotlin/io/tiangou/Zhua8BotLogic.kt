package io.tiangou

import net.mamoe.mirai.console.plugin.jvm.JvmPluginDescription
import net.mamoe.mirai.console.plugin.jvm.KotlinPlugin
import net.mamoe.mirai.event.*
import net.mamoe.mirai.event.events.BotInvitedJoinGroupRequestEvent
import net.mamoe.mirai.event.events.GroupMessageEvent
import net.mamoe.mirai.message.data.*

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
    private var isAtEventConfigure = false;
    private var isAcceptEventConfigure = false;
    override fun onEnable() {
        logger.info { "zhua8机器人,版本号:$VERSION" }
        configureAtEvent()
        configureAcceptEvent()
    }

    private fun configureAtEvent() {
        if (!isAtEventConfigure.apply{ isAtEventConfigure = true }) {
            logger.info { "开始配置zhua8机器人群at消息监听行为" }
            GlobalEventChannel
                // priority = EventPriority.MONITOR 该选项为设置并发优先级
                .subscribeAlways<GroupMessageEvent> (priority = EventPriority.MONITOR) {
                    // 如果消息中能够获取到At对象实例,且At目标为Bot
                    if (message.firstIsInstanceOrNull<At>()?.target == bot.id) {
                        logger.info("接受到at,消息内容:[${message.content}],群名称:${group.name},at人群名片:[${senderName}]")
                        val replyMessageChain = At(sender.id).plus("窝嫩爹");
                        if (group.sendMessage(replyMessageChain).isToGroup) {
                            logger.info("成功回复at人消息:${replyMessageChain.content},群名称:${group.name},at人群名片:[${senderName}]")
                        }
                    }
                }
            logger.info{"zhua8机器人at监听行为配置完成"}
            return
        }
        logger.warning("zhua8机器人at监听已配置,不重复设置监听行为,结束");
    }

    private fun configureAcceptEvent() {
        if (!isAcceptEventConfigure.apply{ isAcceptEventConfigure = true }) {
            logger.info { "开始配置zhua8机器人群邀请监听行为" }
            // priority = EventPriority.MONITOR 该选项为设置并发优先级
            GlobalEventChannel.subscribeAlways<BotInvitedJoinGroupRequestEvent>(priority = EventPriority.MONITOR) {
                logger.info("监听到入群邀请,邀请人:${invitorNick},群号:$groupId, 群名称:$groupName")
                accept()
            }
            logger.info{"zhua8机器人群邀请监听行为配置完成"}
            return
        }
        logger.warning("zhua8机器人邀请监听已配置,不重复设置监听行为,结束");
    }
}
