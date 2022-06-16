@file:JvmName("globalFunctionKt")
@file:JvmMultifileClass

package io.tiangou

import io.tiangou.enums.ErrorCodeEnum
import io.tiangou.expection.Zhua8BotException


public fun check(value: Boolean?, errorEnum: ErrorCodeEnum = ErrorCodeEnum.SYSTEM_ERROR, customizeMessage: String? = null)
{
    if (value == null || value.not()) {
        throw Zhua8BotException(errorEnum, customizeMessage ?: errorEnum.errorMessage)
    }
}