package io.tiangou.service.factory

import io.tiangou.data.Zhua8MessageInfo
import io.tiangou.enums.ErrorCodeEnum
import io.tiangou.enums.OperationEnum
import io.tiangou.expection.Zhua8BotException
import io.tiangou.service.OperationService
import net.mamoe.mirai.event.events.MessageEvent

interface OperationServiceFactory{


    fun createOperationService(messageInfo: Zhua8MessageInfo, operation: OperationEnum) : OperationService


    companion object {
        private val factoryMap: Map<FactoryEnum, OperationServiceFactory> = mapOf(
            FactoryEnum.DEFAULT to DefaultOperationFactory
        )

        fun getFactory(factoryEnum: FactoryEnum): OperationServiceFactory =
            factoryMap.get(factoryEnum) ?: throw Zhua8BotException(ErrorCodeEnum.UNKNOWN_OPERATION_FACTORY_TYPE)
    }

}
enum class FactoryEnum {
    DEFAULT
}