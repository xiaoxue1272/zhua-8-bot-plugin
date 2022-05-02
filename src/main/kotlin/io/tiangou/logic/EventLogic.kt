package io.tiangou.logic


/**
 * 定义监听事件的事件,及其执行逻辑
 *
 * question : 目前存在一个疑问点 listener是否为单例? 若EventLogic具体实现我声明为单例,是否会影响性能?
 *
 * uncertainly : 目前实现为事件拦截及拦截后逻辑统一由io.tiangou.logic.AbstractEventLogic.logic()实现,粒度是否可以拆的更细?
 */
interface EventLogic {

    fun loadLogic()

    fun destroyLogic()

    companion object {
        val eventLogicList : List<EventLogic> = listOf(
            GroupMessageEventLogic(),
            BotInvitedJoinGroupRequestEventLogic(),
            FriendMessageEventLogic(),
            StrangerMessageEventLogic(),
            NewFriendRequestEventLogic(),
            GroupTempMessageEventLogic(),
        )
    }
}
