package io.tiangou.service

/**
 * 操作实现service
 * 注:目前所有操作都是基于MessageEvent 也就是消息事件,因为默认所有操作都是通过消息来触发的
 * 扩展性待考虑,后期是否优化待考虑
 */
interface OperationService {

    fun doOperator() : String

}