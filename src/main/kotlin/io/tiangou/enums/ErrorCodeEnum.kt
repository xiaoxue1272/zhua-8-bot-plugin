package io.tiangou.enums

enum class ErrorCodeEnum(
    val errorCode: String, val errorMessage: String) {
    SYSTEM_ERROR("SYSTEM_ERROR", "系统错误"),
    UNKNOW_OPERATION_TYPE("UNKNOW_OPERATION_TYPE", "未知的操作类型,请重新输入"),
}