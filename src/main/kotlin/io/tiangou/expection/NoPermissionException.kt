package io.tiangou.expection

import io.tiangou.enums.ErrorCodeEnum

class NoPermissionException(errorCodeEnum: ErrorCodeEnum): Zhua8BotException(errorCodeEnum) {

    constructor() : this(ErrorCodeEnum.NO_PERMISSION)

}