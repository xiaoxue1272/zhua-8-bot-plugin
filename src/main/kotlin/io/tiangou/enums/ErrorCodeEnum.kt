package io.tiangou.enums

enum class ErrorCodeEnum(
    val errorCode: String, val errorMessage: String) {
    SYSTEM_ERROR("SYSTEM_ERROR", "系统错误"),
    NO_PERMISSION("NO_PERMISSION", "权限不足"),
    UNKNOWN_OPERATION_TYPE("UNKNOW_OPERATION_TYPE", "未知的操作类型,请重新输入"),
    UNKNOWN_OPERATION_FACTORY_TYPE("UNKNOW_OPERATION_FACTORY_TYPE", "未找到当前配置的操作执行器构造工厂,请检查"),
}