package io.tiangou.enums

enum class ErrorCodeEnum(
    val errorCode: String, val errorMessage: String) {
    SYSTEM_ERROR("SYSTEM_ERROR", "系统错误"),
    NO_PERMISSION("NO_PERMISSION", "权限不足"),
    UNKNOWN_OPERATION_TYPE("UNKNOW_OPERATION_TYPE", "未知的操作类型,请重新输入"),
    COMMAND_LINES_IS_EMPTY("COMMAND_LINES_IS_EMPTY", "指令为空,请重新输入"),
    COMMAND_LINES_ERROR("COMMAND_LINES_ERROR", "指令格式有误,请重新输入"),
    UNKNOWN_OPERATION_FACTORY_TYPE("UNKNOW_OPERATION_FACTORY_TYPE", "未找到当前配置的操作执行器构造工厂,请检查"),
    CREATE_SSH_CLIENT_ERROR("CREATE_SSH_CLIENT_ERROR", "创建ssh连接客户端时异常"),
    SSH_CLIENT_IS_USING("SSH_CLIENT_IS_USING", "当前ssh客户端整在被其他用户使用"),
    SSH_CLIENT_HOST_NULL("SSH_CLIENT_HOST_NULL", "创建ssh连接客户端未指定服务器地址"),
    SSH_CLIENT_PASSWORD_NULL("SSH_CLIENT_PASSWORD_NULL", "创建ssh连接客户端未指定服务器密码"),
    SSH_CLIENT_FLAG_NULL("SSH_CLIENT_FLAG_NULL", "创建ssh连接客户端时未指定标识"),
    SSH_CLIENT_NOT_EXISTS("SSH_CLIENT_NOT_EXISTS", "当前指定ssh客户端不存在,请先创建"),
    CREATE_SSH_CLIENT_CHANNEL_FAILED("CREATE_SSH_CLIENT_CHANNEL_FAILED", "与服务器开启ssh通讯管道失败,请稍后重试"),
    RUN_COMMAND_PARAMETERS_ERROR("RUN_COMMAND_PARAMETERS_ERROR", "指定的参数不正确,请检查"),
    STREAM_ERROR("STREAM_ERROR", "操作流异常"),
}