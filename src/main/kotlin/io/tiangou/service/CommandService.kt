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

    val qqNumber = messageInfo.sender.id

    override fun init(): CommandService {
        val lastUserCommandWork = userLastTaskNoMap.get(qqNumber)
        var workType: Work.WorkType = lastUserCommandWork?.workType() ?: Work.WorkType.SYNC
        var isLifeCycleFinish = lastUserCommandWork?.isLifeCycleFinish() ?: true
        var clientFlag = lastUserCommandWork?.parameters?.clientFlag ?: Constants.DEFAULT_CLIENT_FLAG
        val commandList: List<String>? = messageInfo.body.takeIf {
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
                result = result.replace(this, Constants.EMPTY_STRING).trimStart().trimEnd()
                clientFlag = removeRange(0..1)
            }
            val commandLines = mutableListOf<String>()
            for (matchValue in result.replace(Constants.SQUARE_BRACKET_PREFIX, Constants.EMPTY_STRING)
                .split(Constants.SQUARE_BRACKET_SUFFIX)) {
                val commandLine = matchValue.trimStart().trimEnd()
                if (commandLine.contains(Constants.SQUARE_BRACKET_PREFIX) && commandLine.contains(Constants.SQUARE_BRACKET_SUFFIX)) {
                    throw Zhua8BotException(ErrorCodeEnum.COMMAND_LINES_ERROR)
                }
                if (commandLine.isNotBlank()) {
                    commandLines.add(commandLine + "\n")
                }
            }
            commandLines
        }

        commonWork = CommonWork(lastUserCommandWork?.taskNo ?: "cmd:${Date().time}", ShellInfo(clientFlag, commandList), workType, isLifeCycleFinish)
        userLastTaskNoMap.put(qqNumber, commonWork)
        return this
    }

    override fun execute(): List<String> {
        ShellEngine.execute(commonWork)
        if (commonWork.isLifeCycleFinish()) {
            userLastTaskNoMap.remove(qqNumber)
        }
        return commonWork.result ?: arrayListOf("执行中,目前无法查看执行结果,待后续开发")
    }

    override fun checkUserPermission(messageInfo: Zhua8MessageInfo): CommandService {
//        logger.info("功能没开发好,一律拦截")
//        throw NoPermissionException()
        return this
    }

    companion object {
        @JvmStatic
        private val userLastTaskNoMap: ConcurrentHashMap<Long, CommonWork<ShellInfo, List<String>>> = ConcurrentHashMap()
    }
}