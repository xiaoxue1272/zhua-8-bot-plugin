package io.tiangou.constants
class Constants {
    companion object {

        const val EMPTY_STRING: String = ""

        @JvmStatic
        val OPERATION_REGEX = Regex("/\\w*")

        /**
         * shell连接的声明周期前缀
         * 值true false
         */
        @JvmStatic
        val LIFE_CYCLE_FINISH_PREFIX = Regex("-d\\w*")

        /**
         * shell连接的引擎执行方式
         * 值sync Async
         * 异步执行时,指定的shell声明周期永远为一次性 也就是true
         */
        @JvmStatic
        val ENGINE_EXECUTE_TYPE_PREFIX = Regex("-e\\w*")

        /**
         * shell连接使用的客户端标识
         */
        @JvmStatic
        val USE_CLIENT_PREFIX = Regex("-c\\w*")

        val COMMAND_CONTEXT_PREFIX = Regex("('.*')")

        const val APOSTROPHE : String = "'"

        const val DEFAULT_CLIENT_FLAG : String = "default"
    }
}