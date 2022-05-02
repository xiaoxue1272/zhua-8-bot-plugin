package io.tiangou.expection

import io.tiangou.enums.ErrorCodeEnum

open class Zhua8BotException(errorCodeEnum: ErrorCodeEnum, exception: Exception?) : RuntimeException() {
    val errorCode: String = errorCodeEnum.errorCode
    val errorMessage: String = errorCodeEnum.errorMessage
    val primaryException : Exception? = exception;

    constructor(errorCodeEnum: ErrorCodeEnum): this(errorCodeEnum, null);

}