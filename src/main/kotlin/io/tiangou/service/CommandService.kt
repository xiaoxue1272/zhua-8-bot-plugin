package io.tiangou.service

import io.tiangou.constants.Constants
import io.tiangou.data.Zhua8MessageInfo
import io.tiangou.data.ssh.ShellInfo
import io.tiangou.engine.CommonWork
import io.tiangou.engine.Work
import io.tiangou.engine.ssh.shell.ShellEngine
import io.tiangou.enums.ErrorCodeEnum
import io.tiangou.expection.Zhua8BotException
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class CommandService(messageInfo: Zhua8MessageInfo): AbstractOperationService(messageInfo) {

    private lateinit var commonWork: CommonWork<ShellInfo, List<String>>

    val qqNumber = messageInfo.sender.id;

    override fun init(): CommandService {
        var workType: Work.WorkType = Work.WorkType.SYNC
        var isLifeCycleFinish = true
        var clientFlag = Constants.DEFAULT_CLIENT_FLAG
        val commandList = messageInfo.body.takeIf {
            messageInfo.body.isNotBlank()
        }?.run {
            var result = this
            Constants.ENGINE_EXECUTE_TYPE_PREFIX.find(result)?.value?.apply {
                result = result.replace(this, Constants.EMPTY_STRING)
                val engineExecuteType = removeRange(0..1).uppercase()
                if ("SYNC" == engineExecuteType || "ASYNC" == engineExecuteType) {
                    workType = Work.WorkType.valueOf(engineExecuteType)
                } else {
                    throw Zhua8BotException(ErrorCodeEnum.RUN_COMMAND_PARAMETERS_ERROR)
                }
            }
            Constants.LIFE_CYCLE_FINISH_PREFIX.find(result)?.value?.apply {
                result = result.replace(this, Constants.EMPTY_STRING)
                val isLifeCycleFinishString = removeRange(0..1).uppercase()
                if ("TRUE" == isLifeCycleFinishString || "FALSE" == isLifeCycleFinishString) {
                    if (workType != Work.WorkType.ASYNC) {
                        isLifeCycleFinish = "TRUE" == isLifeCycleFinishString
                    }
                }
                else {
                    throw Zhua8BotException(ErrorCodeEnum.RUN_COMMAND_PARAMETERS_ERROR)
                }
            }
            Constants.USE_CLIENT_PREFIX.find(result)?.value?.apply {
                result = result.replace(this, Constants.EMPTY_STRING)
                clientFlag = removeRange(0..1)
            }
            val commandLines = mutableListOf<String>()
            for (commandLineMather in Constants.COMMAND_CONTEXT_PREFIX.findAll(result).iterator()) {
                commandLines.add(commandLineMather.value.replace(Constants.APOSTROPHE, Constants.EMPTY_STRING).plus("\n"))
            }
            commandLines
        }?.takeIf {
            it.isNotEmpty()
        } ?: throw Zhua8BotException(ErrorCodeEnum.COMMAND_LINES_IS_EMPTY)

        var taskNo = userLastTaskNoMap.get(qqNumber)
        if (taskNo == null) {
            taskNo = "cmd:${Date().time}"
            userLastTaskNoMap.put(qqNumber, taskNo)
        }
        commonWork = CommonWork(taskNo, ShellInfo(clientFlag, commandList), workType, isLifeCycleFinish)
        return this
    }

    override fun execute(): List<String> {
        ShellEngine.execute(commonWork)
        if (commonWork.isLifeCycleFinish()) {
            userLastTaskNoMap.remove(qqNumber)
        }
        return arrayListOf("执行中,目前无法查看执行结果,待后续开发")
    }

    override fun checkUserPermission(messageInfo: Zhua8MessageInfo): CommandService {
//        logger.info("功能没开发好,一律拦截")
//        throw NoPermissionException()
        return this
    }

    companion object {
        @JvmStatic
        private val userLastTaskNoMap: ConcurrentHashMap<Long,String> = ConcurrentHashMap()
    }
}