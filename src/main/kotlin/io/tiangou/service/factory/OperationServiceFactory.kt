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
            Pair(FactoryEnum.DEFAULT, DefaultOperationFactory)
        )

        fun getFactory(factoryEnum: FactoryEnum): OperationServiceFactory {
            val operationServiceFactory = factoryMap.get(factoryEnum);
            if (operationServiceFactory == null) {
                throw Zhua8BotException(ErrorCodeEnum.UNKNOWN_OPERATION_FACTORY_TYPE)
            }
            return operationServiceFactory
        }
    }

}
enum class FactoryEnum {
    DEFAULT
}